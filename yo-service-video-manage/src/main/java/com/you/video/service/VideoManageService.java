package com.you.video.service;

import com.alibaba.fastjson.JSONObject;
import com.you.domain.cms.CmsPage;
import com.you.domain.cms.response.CmsPageResult;
import com.you.domain.filesystem.FileSystemEntity;
import com.you.domain.filesystem.response.FileSystemCode;
import com.you.domain.media.MediaFileEntity;
import com.you.domain.media.VideoEntity;
import com.you.domain.media.response.MediaCode;
import com.you.domain.video.VideoDetailEntity;
import com.you.domain.video.VideoPicEntity;
import com.you.domain.video.VideoPubEntity;
import com.you.dto.video.VideoQueryByIdVO;
import com.you.dto.video.VideoQueryDTO;
import com.you.dto.video.VideoQueryVO;
import com.you.exception.ExceptionCast;
import com.you.utils.BeanUtils;
import com.you.utils.CollectionUtils;
import com.you.video.client.CmsClient;
import com.you.video.client.FileRemoteClient;
import com.you.video.dao.VideoDetailRepository;
import com.you.video.dao.VideoPicRepository;
import com.you.video.dao.VideoPubRepository;
import com.you.video.dao.VideoRepository;
import com.you.vo.RestResultVO;
import com.you.vo.video.PlayVideoVO;
import com.you.vo.video.VideoModelVO;
import com.you.vo.video.VideoPreviewVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:21
 */
@Service
public class VideoManageService {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    VideoDetailRepository videoDetailRepository;

    @Autowired
    VideoPicRepository videoPicRepository;

    @Autowired
    FileRemoteClient fileRemoteClient;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    CmsClient cmsClient;

    @Autowired
    VideoPubRepository videoPubRepository;

    @Value("${yo-service-video-manage.upload-location}")
    String uploadPath;

    @Value("${yo-service-video-manage.video-site-id}")
    String videoSite;

    @Value("${yo-service-video-manage.video-template-id}")
    String videoTemplate;

    @Value("${yo-service-video-manage.video-pageWebPath}")
    String videoWebPath;

    @Value("${yo-service-video-manage.video-pagePhysicalPath}")
    String videoPhysicalPath;

    @Value("${yo-service-video-manage.video-dataModel-url}")
    String videoDataUrl;

    @Value("${yo-service-video-manage.video-preViewUrl}")
    String videoPreViewUrl;

    /**
     * 查询视频信息列表
     * @return
     */
    public Map<String, Object> listVideos(VideoQueryDTO videoQueryDTO) {
        if (null == videoQueryDTO) {
            return null;
        }
        Integer pageNum = videoQueryDTO.getPageNum();
        if (pageNum <= 0) {
            pageNum = 1;
        }
        pageNum = pageNum - 1;
        Integer pageSize = videoQueryDTO.getPageSize();
        if (pageSize <= 0) {
            pageSize = 20;
        }

        // Mongo查询符合条件
        Query query = new Query();
        if (StringUtils.isNotEmpty(videoQueryDTO.getVideoName())) {
            query.addCriteria(Criteria.where("fileOriginalName").regex(".*?\\" +videoQueryDTO.getVideoName()+ ".*"))
                    .with(Sort.by(Sort.Direction.ASC, "uploadTime"));
        }
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        List<MediaFileEntity> videoEntities = mongoTemplate.find(query.with(pageRequest), MediaFileEntity.class);
        long total = mongoTemplate.count(query, "media_file");

        // 数据库查询符合条件
        List<VideoQueryVO> resultList = new ArrayList<>();
        if (null != videoQueryDTO.getTagId()) {
            List<VideoDetailEntity> detailEntities = videoDetailRepository.findByTagId(videoQueryDTO.getTagId());
            total = detailEntities.size();
            List<String> idList = new ArrayList<>();
            detailEntities.stream().forEach(videoDetailEntity -> {
                idList.add(videoDetailEntity.getId());
            });

            // Mongo查询的数据和数据库查询的数据过滤
            for (MediaFileEntity entity : videoEntities) {
                if (CollectionUtils.contains(idList.iterator(), entity.getFileId())) {
                    resultList.add(BeanUtils.clone(entity, VideoQueryVO.class));
                }
            }
        } else {
            videoEntities.stream().forEach(mediaFileEntity -> {
                resultList.add(BeanUtils.clone(mediaFileEntity, VideoQueryVO.class));
            });
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultList", resultList);
        resultMap.put("total", total);
        return resultMap;
    }

    /**
     * 后台管理员上传视频
     * @param file
     * @param videoName
     * @param tagId
     * @param videoDesc
     * @param videoId
     * @return
     */
    @Transactional
    public String uploadVideo(MultipartFile file, String videoName, Integer tagId, String videoDesc, String videoId) {
        Optional<MediaFileEntity> optional = videoRepository.findById(videoId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(MediaCode.FILE_IS_NOT_UPLOAD);
        }
        MediaFileEntity fileEntity = optional.get();
        // 设置视频名
        String newOriginalName = fileEntity.getFileOriginalName();
        if (StringUtils.isNotEmpty(videoName)) {
            newOriginalName = videoName + "." + fileEntity.getFileType();
            fileEntity.setFileOriginalName(newOriginalName);
        }
        // 调用文件系统存储视频封面到FastFdfs
        RestResultVO<FileSystemEntity> resultVO = fileRemoteClient.upload("video", videoId, null, file);
        String picId = resultVO.getData().getFileId();

        VideoPicEntity videoPicEntity = new VideoPicEntity();
        videoPicEntity.setVideoId(videoId);
        videoPicEntity.setPicId(picId);

        // 保存视频封面关联到数据库
        videoPicRepository.save(videoPicEntity);

        VideoDetailEntity videoDetail = new VideoDetailEntity();
        videoDetail.setId(videoId);
        videoDetail.setTagId(tagId);
        videoDetail.setVideoDesc(videoDesc);
        videoDetail.setVideoName(newOriginalName);
        videoDetail.setUploadTime(new Date());
        // 保存视频信息关联到数据库
        videoDetailRepository.save(videoDetail);

        fileEntity.setImgPath(picId);
        videoRepository.save(fileEntity);
        return "操作成功！";
    }

    /**
     * 通过Id删除视频
     * @param fileId
     * @return
     */
    @Transactional
    public String deleteById(String fileId) {
        Optional<MediaFileEntity> optional = videoRepository.findById(fileId);
        boolean delStatus = false;
        if (optional.isPresent()) {
            String filePath = optional.get().getFilePath();
            //通过物理路径删除视频
            delStatus = delVideoByPhysicalPath(filePath);
        }
        if (false == delStatus) {
            ExceptionCast.cast(FileSystemCode.FS_DELETEFILE_DBFAIL);
        }
        videoDetailRepository.deleteById(fileId);
        videoPicRepository.deleteById(fileId);
        videoRepository.deleteById(fileId);
        return "删除成功！";
    }

    /**
     * 通过物理路径删除视频
     * @param filePath
     * @return
     */
    private boolean delVideoByPhysicalPath(String filePath) {
        String videoPath = uploadPath + filePath;
        File file = new File(videoPath);
        boolean status = delVideo(file);
        return status;
    }

    /**
     * 递归删除视频文件
     * @param file
     * @return
     */
    private boolean delVideo(File file) {
        File[] files = file.listFiles();
        List<File> list = Arrays.asList(files);
        for (File file1 : list) {
            if (!file1.exists() || null == file) {
                return true;
            }
            if (file1.isDirectory()) {
                delVideo(file1);
            } else {
                file1.delete();
            }
        }
        file.delete();
        return true;
    }

    /**
     * 修改视频信息
     * @param file
     * @param videoName
     * @param tagId
     * @param videoId
     * @param videoDesc
     * @return
     */
    @Transactional
    public String editVideo(MultipartFile file, String videoName, Integer tagId, String videoId, String videoDesc) {
        // 查询Mongo视频信息
        MediaFileEntity mediaFileEntity = this.findByIdInMongo(videoId);
        if (StringUtils.isNotEmpty(videoName)) {
            videoName = videoName + "." + mediaFileEntity.getFileType();
        } else {
            videoName = mediaFileEntity.getFileOriginalName();
        }

        VideoDetailEntity detailEntity = this.findDetailById(videoId);
        // 修改视频详情信息
        detailEntity.setVideoName(videoName);
        detailEntity.setVideoDesc(videoDesc);
        detailEntity.setTagId(tagId);
        videoDetailRepository.save(detailEntity);

        // 修改视频封面信息
        VideoPicEntity videoPicEntity = this.findVideoPicById(videoId);
        RestResultVO<FileSystemEntity> vo = fileRemoteClient.upload("video", videoId, null, file);
        String picId = vo.getData().getFileId();
        videoPicEntity.setPicId(picId);
        videoPicRepository.save(videoPicEntity);

        // 修改Mongo视频信息
        mediaFileEntity.setFileOriginalName(videoName);
        mediaFileEntity.setImgPath(picId);
        videoRepository.save(mediaFileEntity);

        return "操作成功！";
    }

    /**
     * 通过Id查询视频信息
     * @param fileId
     * @return
     */
    public VideoQueryByIdVO findById(String fileId) {
        // 查询视频详情信息
        VideoDetailEntity videoDetailEntity = this.findDetailById(fileId);
        VideoQueryByIdVO resultVO = BeanUtils.clone(videoDetailEntity, VideoQueryByIdVO.class);
        String videoName = videoDetailEntity.getVideoName();
        // 去掉视频名后缀 test.mp4 -> test
        String name = videoName.substring(0, videoName.lastIndexOf("."));
        resultVO.setVideoName(name);
        // 查询视频封面信息
        VideoPicEntity videoPicEntity = this.findVideoPicById(fileId);
        String picId = videoPicEntity.getPicId();
        resultVO.setPicId(picId);
        return resultVO;
    }

    /**
     * 查询Mongo视频信息
     * @param videoId
     * @return
     */
    private MediaFileEntity findByIdInMongo(String videoId) {
        Optional<MediaFileEntity> optional = videoRepository.findById(videoId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(MediaCode.FILE_IS_NOT_EXIST);
        }
        return optional.get();
    }

    /**
     * 查询视频详情信息
     * @param videoId
     * @return
     */
    private VideoDetailEntity findDetailById(String videoId) {
        Optional<VideoDetailEntity> optional = videoDetailRepository.findById(videoId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(MediaCode.FILE_IS_NOT_EXIST);
        }
        return optional.get();
    }

    /**
     * 查询视频封面信息
     * @param videoId
     * @return
     */
    private VideoPicEntity findVideoPicById(String videoId) {
        Optional<VideoPicEntity> videoPicOptional = videoPicRepository.findById(videoId);
        Optional<MediaFileEntity> id = videoRepository.findById(videoId);
        return videoPicOptional.get();
    }

    /**
     * 通过Id查询视频数据模型信息
     * @param videoId
     * @return
     */
    public VideoModelVO findVideoModelById(String videoId) {
        // 从数据库查询视频信息
        VideoQueryByIdVO videoQueryByIdVO = this.findById(videoId);
        // 从Mongo查询视频信息，获取视频播放路径
        MediaFileEntity mediaFileEntity = this.findByIdInMongo(videoId);
        VideoModelVO videoModelVO = new VideoModelVO();
        PlayVideoVO playVideoVO = BeanUtils.clone(videoQueryByIdVO, PlayVideoVO.class);
        // 设置视频播放路径
        playVideoVO.setPlayUrl(videoQueryByIdVO.getVideoUrl());
        playVideoVO.setVideoUrl(mediaFileEntity.getFileUrl());
        videoModelVO.setPlayVideoVO(playVideoVO);

        // 通过视频标签Id，查询8条数据作为猜猜你喜欢
        List<PlayVideoVO> playVideoVOList = this.getPlayVideoVOList(videoQueryByIdVO.getTagId());
        videoModelVO.setPlayVideoVOList(playVideoVOList);

        return videoModelVO;
    }

    /**
     * 通过视频标签Id，查询8条数据作为猜猜你喜欢
     * @param tagId
     * @return
     */
    private List<PlayVideoVO> getPlayVideoVOList(Integer tagId) {
        List<PlayVideoVO> playVideoVOList = new ArrayList<>();
        List<VideoDetailEntity> detailEntities = videoDetailRepository.findByTagId(tagId);

        // 过滤8条数据作为猜猜你喜欢
        int index = 0;
        for (VideoDetailEntity detailEntity : detailEntities) {
            if (index >= 8) {
                break;
            }
            MediaFileEntity mediaFileEntity = this.findByIdInMongo(detailEntity.getId());

            VideoQueryByIdVO queryByIdVO = this.findById(detailEntity.getId());
            PlayVideoVO playVideoVO = BeanUtils.clone(queryByIdVO, PlayVideoVO.class);
            playVideoVO.setVideoUrl(mediaFileEntity.getFileUrl());
            playVideoVO.setPlayUrl(queryByIdVO.getVideoUrl());

            playVideoVOList.add(playVideoVO);
            index ++;
        }
        return playVideoVOList;
    }

    /**
     * 视频页面预览
     * @param videoId
     * @return
     */
    public String addVideoPage(String videoId) {
        // 构建视频Cms信息
        CmsPage videoCmsPage = this.buildVideoPage(videoId);

        CmsPageResult cmsPageResult = cmsClient.insertPage(videoCmsPage);
        if (!cmsPageResult.isSuccess()) {
            return "添加失败！";
        }
        String pageId = cmsPageResult.getCmsPage().getPageId();
        String videoReViewUrl = videoPreViewUrl + pageId;

        return videoReViewUrl;
    }

    /**
     * 发布视频页面
     * @param videoId
     * @return
     */
    @Transactional
    public String postVideoPage(String videoId) {
        // 构建视频Cms信息
        CmsPage videoPage = this.buildVideoPage(videoId);
        Optional<VideoDetailEntity> optional = videoDetailRepository.findById(videoId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(MediaCode.FILE_IS_NOT_EXIST);
        }
        VideoDetailEntity videoDetailEntity = optional.get();
        if (202002 == videoDetailEntity.getStatus()) {
            // 视频已发布
            ExceptionCast.cast(MediaCode.FILE_IS_POSTED);
        }
        // 发布视频页面
        RestResultVO<String> resultVO = cmsClient.videoPostPage(videoPage);
        // 更新课程状态
        videoDetailEntity.setStatus(202002);
        videoDetailEntity.setVideoUrl("http://" + resultVO.getData());
        videoDetailRepository.save(videoDetailEntity);
        this.createVideoPub(videoDetailEntity);
        String videoUrl = resultVO.getData();
        return videoUrl;
    }

    /**
     * 构建CMS页面信息
     * @param videoId
     * @return
     */
    private CmsPage buildVideoPage(String videoId) {
        this.findByIdInMongo(videoId);
        VideoDetailEntity videoDetailEntity = this.findDetailById(videoId);
        String videoName = videoDetailEntity.getVideoName();
        // 保存视频页面信息到Cms
        CmsPage videoCmsPage = new CmsPage();
        videoCmsPage.setSiteId(videoSite);
        // 页面别名，默认视频名称
        String videoPageAliase = videoName.substring(0, videoName.lastIndexOf("."));
        videoCmsPage.setPageAliase(videoPageAliase);
        videoCmsPage.setPageName(videoId + ".html");
        videoCmsPage.setTemplateId(videoTemplate);
        videoCmsPage.setPageWebPath(videoWebPath);
        videoCmsPage.setPagePhysicalPath(videoPhysicalPath);
        videoCmsPage.setDataUrl(videoDataUrl + videoId);
        videoCmsPage.setPageCreateTime(new Date());

        return videoCmsPage;
    }

    /**
     * 首页查询视频列表
     * @return
     */
    public Map<String, List<PlayVideoVO>> listIndexVideos() {
        Specification<VideoDetailEntity> spec = new Specification<VideoDetailEntity>() {
            @Override
            public Predicate toPredicate(Root<VideoDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<VideoDetailEntity> path = root.get("status");
                // 已发布
                Predicate predicate = criteriaBuilder.equal(path, "202002");
                return predicate;
            }
        };

        // 按上传时间降序查询
        Sort sort = new Sort(Sort.Direction.DESC, "uploadTime");
        Pageable pageable = PageRequest.of(0, 4, sort);

        // 查询最近更新
        Page<VideoDetailEntity> pages = videoDetailRepository.findAll(spec, pageable);
        List<VideoDetailEntity> entities = pages.getContent();
        //最近更新
        List<PlayVideoVO> newVideos = this.getPic(entities);

        // 电影
        List<PlayVideoVO> movieVideos = this.findVideosByTagId(spec, pageable, 109);

        // 动漫
        List<PlayVideoVO> aniVideos = this.findVideosByTagId(spec, pageable, 3);

        // 综艺
        List<PlayVideoVO> envVideos = this.findVideosByTagId(spec, pageable, 2);

        Map<String, List<PlayVideoVO>> resultMap = new HashMap<>();
        resultMap.put("new", newVideos);
        resultMap.put("movie", movieVideos);
        resultMap.put("anima", aniVideos);
        resultMap.put("arts", envVideos);
        return resultMap;
    }

    /**
     * 通过标签Id查询视频信息
     * @return
     */
    private List<PlayVideoVO> findVideosByTagId(Specification spec, Pageable pageable, Integer tagId) {
        Specification<VideoDetailEntity> spec2 = new Specification<VideoDetailEntity>() {
            @Override
            public Predicate toPredicate(Root<VideoDetailEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<VideoDetailEntity> path = root.get("tagId");
                // 标签Id
                Predicate predicate = criteriaBuilder.equal(path, tagId);
                return predicate;
            }
        };

        Specification specification = spec.and(spec2);
        Page<VideoDetailEntity> page = videoDetailRepository.findAll(specification, pageable);
        List<PlayVideoVO> playVideoVOS = this.getPic(page.getContent());
        return playVideoVOS;
    }

    /**
     * 获取封面
     * @param entities
     * @return
     */
    private List<PlayVideoVO> getPic(List<VideoDetailEntity> entities) {
        List<PlayVideoVO> list = new ArrayList<>();

        entities.stream().forEach(videoDetailEntity -> {
            PlayVideoVO playVideoVO = BeanUtils.clone(videoDetailEntity, PlayVideoVO.class);
            Optional<VideoPicEntity> optional = videoPicRepository.findById(videoDetailEntity.getId());
            String picUrl = "http://192.168.204.130:8888/" + optional.get().getPicId();
            playVideoVO.setPicId(picUrl);
            list.add(playVideoVO);
        });

        return list;
    }

    /**
     * 构建ES数据
     * @return
     */
    private void createVideoPub(VideoDetailEntity videoDetailEntity) {
        VideoPubEntity videoPubEntity = BeanUtils.clone(videoDetailEntity, VideoPubEntity.class);
        videoPubEntity.setTimestamp(new Date());

        VideoPicEntity videoPicEntity = this.findVideoPicById(videoDetailEntity.getId());
        videoPubEntity.setPicId(videoPicEntity.getPicId());
        videoPubRepository.save(videoPubEntity);
    }
}
