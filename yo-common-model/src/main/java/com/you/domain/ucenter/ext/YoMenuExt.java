package com.you.domain.ucenter.ext;

import com.you.domain.course.ext.CategoryNode;
import com.you.domain.ucenter.YoMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by MichaelLiu on 2019/3/20.
 */
@Data
@ToString
public class YoMenuExt extends YoMenu {

    List<CategoryNode> children;
}
