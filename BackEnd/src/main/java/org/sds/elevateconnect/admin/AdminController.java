package org.sds.elevateconnect.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IDbManagementService dbManagementService;

    @PostMapping("/db/rebuild-database")
    public void rebuildSchema() {
        dbManagementService.rebuildDatabase();
    }

    @PostMapping("/db/rebuild-with-sample-data")
    public void rebuildSchemaWithSampleData() {
        dbManagementService.rebuildDatabase();
        dbManagementService.insertSampleData();
    }

}
