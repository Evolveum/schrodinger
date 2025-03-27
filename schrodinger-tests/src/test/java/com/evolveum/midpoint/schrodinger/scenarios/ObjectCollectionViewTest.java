package com.evolveum.midpoint.schrodinger.scenarios;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

public class ObjectCollectionViewTest extends AbstractSchrodingerTest {

    private static final String COST_CENTER_LOOKUP_TABLE_FILE = "./src/test/resources/objects/lookuptable/lookup-table-cost-center.xml";
    private static final String COST_CENTER_OBJECT_TEMPLATE_FILE = "./src/test/resources/objects/objecttemplate/object-template-for-lookup-table-cost-center.xml";
    private static final String PERSON_VIEW_SYSTEM_CONFIGURATION_FILE = "./src/test/resources/objects/systemconfiguration/system-configuration-person-view-extension.xml";
    private static final String USER_WITH_COST_CENTER_FILE = "./src/test/resources/objects/users/user-with-cost-center.xml";


    @Override
    protected List<File> getObjectListToImport() {
        return List.of(new File(COST_CENTER_LOOKUP_TABLE_FILE), new File(COST_CENTER_OBJECT_TEMPLATE_FILE),
                new File(PERSON_VIEW_SYSTEM_CONFIGURATION_FILE), new File(USER_WITH_COST_CENTER_FILE));
    }

    /**
     * Coverage for the ticket #10483
     * LookupTable label not displayed in Object Collection View list
     */
    @Test
    void test0010createPersonWithCostCenter() {
        basicPage.loggedUser().logoutIfUserIsLogin();
        midPoint.formLogin().login(getUsername(),getPassword());

        basicPage.listUsers("Persons")
                .table()
                .assertTableContainsColumnWithValue("Cost Center", "Cost center 1");
    }
}
