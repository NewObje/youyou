package com.you.video.service;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/**
 * @author Michael Liu
 * @create 2020-04-12 15:45
 */
public class JwtTest {

    @Test
    public void testJwt() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6bnVsbCwiaWQiOiI0OSIsImV4cCI6MTU4NjkwNDMwOCwianRpIjoiYjFjMDcyYWEtMjUxMS00YmRiLTk1ZDAtMWU4Y2I2MDBiNWU0IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.d8S3riEOy1tZUJTlc15LAJk1cAezgNM2SuZullMvXPssn13eZlEbG4KKn-_BJyH67tzz9r6g2mcKRsG_SRUZknRn-8bytwjQfjMmVuLownCyCCl5Hye51SXrvN6qWnKac_WtoroxjdzWn9JLggJ7wm_SVh79pmT1BzOmhhzanZBei-UdjGquHLhhm46VORDyDzAJ6SAFEp_ghXA2px7JpTji9ekKkwfGFkUmJOF_HsZKRQkK5c0-4d41QhZ-2WpZ8BGfM5uXMd67SzYUpWiOW0Opv61WIO40rLwXpCj7CfegPjHjqbcynlDQt5ahhLBb2_w_fEypnddSL9G6p9XfEQ";

        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmEld/G/zs4hdo04VaH7fP62olZXJKl2VsndnVrhyzn6Z2UfZjNDRcO9/06HmkY6lCViihzGOsqylR/RQ+A60lzMYsBJG830QeYVvD/pXAB9MQ9jkfmIiVFw0jMxTstWtBkdV/XUfFoH0m93nYshOgkCeoP4ulTdeSY/qrvumi6WVdQ3gnVtoi7SDrzwbN5JgfhKh9sUsyBtOXd9SNF7KpxeS3GQ3c6+uUtlLDBPvxGiXFndnEvKG2RMENpfHs84txVUdqG9VFhxPD2xpM8GcZujI9ENEPNkUb0YFyJdjW3FsoMzyWCX/ZQyf5JnFbFkmhXx8oL0ro8NG6J+uj4hevQIDAQAB-----END PUBLIC KEY-----";
        //校验jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
