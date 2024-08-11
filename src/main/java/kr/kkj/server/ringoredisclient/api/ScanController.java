package kr.kkj.server.ringoredisclient.api;

import kr.kkj.server.ringoredisclient.RedisUserContext;
import kr.kkj.server.ringoredisclient.model.request.ScanNodeInfo;
import kr.kkj.server.ringoredisclient.model.request.ScanSearchRequest;
import kr.kkj.server.ringoredisclient.model.request.ScansRequest;
import kr.kkj.server.ringoredisclient.model.response.RetrievedKeyResponse;
import kr.kkj.server.ringoredisclient.model.response.ScanResponse;
import kr.kkj.server.ringoredisclient.service.ScanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scan")
@RequiredArgsConstructor
@Slf4j
public class ScanController {

    private final ScanService scanService;

    @PostMapping("/list")
    public List<ScanResponse> scan(RedisUserContext context, @RequestBody ScansRequest request) {
        return scanService.getScanKeys(context,request);
    }

    @GetMapping("/data")
    public RetrievedKeyResponse getData(RedisUserContext context,
                                        @RequestParam String nodeId,
                                        @RequestParam String redisKey) {
        if( !StringUtils.hasText(nodeId) ){
            throw new RuntimeException("EmptyKey");
        }
        if( !StringUtils.hasText(redisKey) ){
            throw new RuntimeException("Empty NodeID");
        }

        var nodeInfo = ScanNodeInfo
                .builder()
                .nodeId(nodeId)
                .redisKey(redisKey)
                .build();
        return scanService.getRedisData(context,nodeInfo);
    }


    @PostMapping("/search")
    public List<ScanResponse> scanSearch(RedisUserContext context,@RequestBody ScanSearchRequest request) {

        if( request.isEmptySearch() ){
            throw new RuntimeException("Empty searchValue");
        }

        scanService.searchRedisKey(context,request);
        return null;
    }

}
