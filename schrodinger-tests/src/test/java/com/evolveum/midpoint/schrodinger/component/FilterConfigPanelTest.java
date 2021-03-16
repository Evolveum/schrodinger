/*
 * Copyright (c) 2010-2021 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.component;

import com.evolveum.midpoint.schrodinger.component.user.UsersPageTable;

import org.testng.annotations.Test;

import com.evolveum.midpoint.schrodinger.AbstractSchrodingerTest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by honchar
 */
public class FilterConfigPanelTest extends AbstractSchrodingerTest {

    private static final File TEST_OBJECT_COLLECTION = new File("./src/test/resources/component/objects/objectcollections/filter-config-test-object-collection.xml");
    private static final File OBJ_REF_PROPERTY_CONFIG_COLLECTION_TEST = new File("./src/test/resources/component/objects/objectcollections/obj-ref-property-config-test.xml");
    private static final File DROPDOWN_PROPERTY_CONFIG_COLLECTION_TEST = new File("./src/test/resources/component/objects/objectcollections/dropdown-property-config-test.xml");
    private static final File OBJECT_COLLECTION_TEST_USER = new File("./src/test/resources/component/objects/users/object-collection-test-user.xml");
    private static final File OBJ_REF_PROPERTY_CONFIG_TEST_USER = new File("./src/test/resources/component/objects/users/obj-ref-property-config-test-user.xml");
    private static final File DROPDOWN_PROPERTY_CONFIG_TEST_USER = new File("./src/test/resources/component/objects/users/dropdown-property-config-test-user.xml");
    private static final File NEW_OBJECT_COLLECTION_TEST_USER = new File("./src/test/resources/component/objects/users/new-object-collection-test-user.xml");
    private static final File SYSTEM_CONFIG_WITH_OBJ_COLLECTIONS = new File("./src/test/resources/configuration/objects/systemconfig/system-configuration-user-obj-collection.xml");

    @Override
    protected List<File> getObjectListToImport(){
        return Arrays.asList(TEST_OBJECT_COLLECTION, OBJECT_COLLECTION_TEST_USER,
                NEW_OBJECT_COLLECTION_TEST_USER, OBJ_REF_PROPERTY_CONFIG_COLLECTION_TEST, OBJ_REF_PROPERTY_CONFIG_TEST_USER,
                DROPDOWN_PROPERTY_CONFIG_COLLECTION_TEST, DROPDOWN_PROPERTY_CONFIG_TEST_USER, SYSTEM_CONFIG_WITH_OBJ_COLLECTIONS);
    }

    @Test
    public void configureFilterForObjectCollection() {
        basicPage
                .listObjectCollections()
                    .table()
                    .clickByName("FilterTestUsers")
                        .selectTabBasic()
                            .form()
                            .showEmptyAttributes("Properties")
                        .and()
                    .and()
                    .configSearch()
                        .setPropertyTextValue("Name", "FilterConfigTest", true)
                        .setPropertyFilterValue("Name", "Equal", true)
                            .confirmConfiguration()
                    .clickSave()
                    .feedback()
                    .isSuccess();

        midPoint.logout();
        midPoint.formLogin().login(username, password);

        UsersPageTable usersPageTable = basicPage.listUsers("FilterTestUsers").table();
        usersPageTable.assertTableObjectsCountEquals(1);
        usersPageTable.assertTableContainsText("FilterConfigTest");
    }

    @Test
    public void createNewObjectCollectionWithConfiguredFilter() {
        basicPage
                .newObjectCollection()
                    .selectTabBasic()
                        .form()
                        .addAttributeValue("Name", "NewObjCollectionTest")
                        .setDropDownAttributeValue("Type", "User")
                    .and()
                .and()
                    .configSearch()
                    .setPropertyTextValue("Name", "NewObjCollection", true)
                    .setPropertyFilterValue("Name", "Starts with", true)
                .confirmConfiguration()
                .clickSave()
                .feedback()
                .assertSuccess();

        basicPage
                .adminGui()
                .addNewObjectCollection("NewObjCollectionTest", "User", "Object collection", "NewObjCollectionTest")
                .feedback()
                .assertSuccess();

        midPoint.logout();
        midPoint.formLogin().login(username, password);

        UsersPageTable usersPageTable = basicPage.listUsers("NewObjCollectionTest").table();
        usersPageTable.assertTableObjectsCountEquals(1);
        usersPageTable.assertTableContainsText("NewObjCollectionTestUser");
    }

    @Test
    public void configureFilterWithObjectReferenceAttribute() {
        basicPage
                .listObjectCollections()
                    .table()
                        .search()
                            .byName()
                            .inputValue("ObjRefAttributeConfigTest")
                            .updateSearch()
                        .and()
                        .clickByName("ObjRefAttributeConfigTest")
                            .selectTabBasic()
                                .form()
                                .showEmptyAttributes("Properties")
                                .and()
                            .and()
                            .configSearch()
                                .setPropertyObjectReferenceValue("Role membership", "00000000-0000-0000-0000-000000000008", true)
                                .confirmConfiguration()
                            .clickSave()
                        .feedback()
                                .isSuccess();
        midPoint.logout();
        midPoint.formLogin().login(username, password);

        UsersPageTable usersPageTable = basicPage.listUsers("ObjRefAttributeConfigTest").table();
        usersPageTable.assertTableObjectsCountEquals(1);
        usersPageTable.assertTableContainsText("ObjRefPropertyConfigUser");
    }

    @Test
    public void configureFilterWithDropdownAttribute() {
        basicPage
                .listObjectCollections()
                    .table()
                        .search()
                            .byName()
                            .inputValue("DropdownPropertyConfigTest")
                            .updateSearch()
                        .and()
                        .clickByName("DropdownPropertyConfigTest")
                            .selectTabBasic()
                                .form()
                                .showEmptyAttributes("Properties")
                                .and()
                            .and()
                            .configSearch()
                                .setPropertyDropdownValue("Administrative status", "DISABLED", true)
                                .setPropertyFilterValue("Administrative status", "Equal", false)
                                .confirmConfiguration()
                            .clickSave()
                        .feedback()
                                .isSuccess();
        midPoint.logout();
        midPoint.formLogin().login(username, password);

        UsersPageTable usersPageTable = basicPage.listUsers("DropdownPropertyConfigTest").table();
        usersPageTable.assertTableObjectsCountEquals(1);
        usersPageTable.assertTableContainsText("DropdownPropertyConfigUser");
    }

}
