package com.lorandi.assembly.resource;

import com.lorandi.assembly.dto.ResultDTO;
import com.lorandi.assembly.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
public class ResultResource {

    private final VoteService service;

    @GetMapping()
    @Operation(summary = "Result by surveyId",
            responses = {@ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ResultDTO.class)))})
    public List<ResultDTO> result()  {
        return service.assemblyResult();
    }



    @GetMapping("/{surveyId}")
    @Operation(summary = "Result by surveyId",
            responses = {@ApiResponse(responseCode = "200", description = "Resource successfully retrieved",
                    content = @Content(schema = @Schema(implementation = ResultDTO.class)))})
    public ResultDTO result(@PathVariable Long surveyId)  {
        return service.surveyResult(surveyId);
    }
}
