package org.sds.elevateconnect.admin;

import org.sds.elevateconnect.mapper.FileMapper;
import org.sds.elevateconnect.service.interfaces.IGcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnExpression("${controllers.adminController.enabled:false}")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IDbManagementService dbManagementService;
    @Autowired
    private IGcsService gcsService;

    @PostMapping("/db/rebuild-database")
    public void rebuildSchema() {
        gcsService.emptyBucket();
        dbManagementService.rebuildDatabase();
    }

    @PostMapping("/db/rebuild-with-sample-data")
    public void rebuildSchemaWithSampleData() {
        rebuildSchema();
        dbManagementService.insertSampleData();
    }

}
