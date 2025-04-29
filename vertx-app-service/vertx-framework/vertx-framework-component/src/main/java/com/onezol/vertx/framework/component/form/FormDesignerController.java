package com.onezol.vertx.framework.component.form;

import com.alibaba.fastjson2.JSONObject;
import com.onezol.vertx.framework.common.model.GenericResponse;
import com.onezol.vertx.framework.support.support.ResponseHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/form-designer")
public class FormDesignerController {

    private static JSONObject FORM_DESIGNER_CACHE = new JSONObject();

    @PostMapping("/save")
    public GenericResponse<JSONObject> saveFormDesigner(@RequestBody JSONObject jsonObject) {
        FORM_DESIGNER_CACHE = jsonObject;
        return ResponseHelper.buildSuccessfulResponse(FORM_DESIGNER_CACHE);
    }

    @GetMapping
    public GenericResponse<JSONObject> getFormDesigner() {
        return ResponseHelper.buildSuccessfulResponse(FORM_DESIGNER_CACHE);
    }
}
