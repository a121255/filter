package com.example.filter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info(" >>>>>>>>> 진입 ");

        /* // 필터에서 리더를 통해 내용을 읽어버렸기 때문에 뒷단에서 더 이상 읽을 수 없는 문제 발생


        var req = new HttpServletRequestWrapper((HttpServletRequest) request); // 실질적으로 컨트롤러에서 받는 req,res는 한 번 변환되어 전달되게 된다
        var res = new HttpServletResponseWrapper((HttpServletResponse) response);

        var br = req.getReader();
        var list = br.lines().collect(Collectors.toList());

        list.forEach(it -> {
            log.info("{}", it);
        });
         */

        var req = new ContentCachingRequestWrapper((HttpServletRequest) request);       // reader로 읽을 때 따로 내부에 contents caching byte array에 내용을 담아둠
        var res = new ContentCachingResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(req, res);

        // 컨트롤러 쪽에서 작업을 다 처리하고 return 나가기 직전에 내용 확인 위해 do.filter 아래에서 내용을 확인함
        var reqJson = new String(req.getContentAsByteArray());
        log.info("req : {}", reqJson);

        var resJson = new String(res.getContentAsByteArray());
        log.info("res: {}", resJson);
        // 클라이언트 측에서 보면 body가 비어있음. 캐싱된 내용이긴 하지만 한 번 읽었기 때문.
        res.copyBodyToResponse(); // 이걸 꼭 해줘야 클라이언트 body에서 확인됨

        log.info(" <<<<<<<<<< 리턴");
    }

}
