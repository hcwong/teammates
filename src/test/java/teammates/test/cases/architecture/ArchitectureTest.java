package teammates.test.cases.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.testng.annotations.Test;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

/**
 * Checks for the system's architectural integrity.
 */
public class ArchitectureTest {

    @Deprecated
    private static final String LEGACY_UI_CONTROLLER_PACKAGE = "teammates.ui.controller";
    @Deprecated
    private static final String LEGACY_PAGEOBJECT_PACKAGE = "teammates.test.pageobjects";
    @Deprecated
    private static final String LEGACY_BROWSERTESTS_PACKAGE = "teammates.test.cases.browsertests";

    private static final JavaClasses ALL_CLASSES = forClasses("teammates");

    private static final String COMMON_PACKAGE = "teammates.common";

    private static final String STORAGE_PACKAGE = "teammates.storage";
    private static final String STORAGE_API_PACKAGE = STORAGE_PACKAGE + ".api";
    private static final String STORAGE_ENTITY_PACKAGE = STORAGE_PACKAGE + ".entity";
    private static final String STORAGE_SEARCH_PACKAGE = STORAGE_PACKAGE + ".search";

    private static final String LOGIC_PACKAGE = "teammates.logic";

    private static final String LOGIC_CORE_PACKAGE = LOGIC_PACKAGE + ".core";
    private static final String LOGIC_API_PACKAGE = LOGIC_PACKAGE + ".api";

    private static final String UI_PACKAGE = "teammates.ui";
    private static final String UI_AUTOMATED_PACKAGE = UI_PACKAGE + ".automated";
    private static final String UI_WEBAPI_PACKAGE = UI_PACKAGE + ".webapi.action";
    private static final String UI_OUTPUT_PACKAGE = UI_PACKAGE + ".webapi.output";
    private static final String UI_REQUEST_PACKAGE = UI_PACKAGE + ".webapi.request";

    private static final String TEST_PACKAGE = "teammates.test";

    private static final String TEST_CASES_PACKAGE = TEST_PACKAGE + ".cases";
    private static final String TEST_DRIVER_PACKAGE = TEST_PACKAGE + ".driver";

    private static final String E2E_PACKAGE = "teammates.e2e";

    private static final String E2E_CASES_PACKAGE = E2E_PACKAGE + ".cases";
    private static final String E2E_PAGEOBJECTS_PACKAGE = E2E_PACKAGE + ".pageobjects";
    private static final String E2E_UTIL_PACKAGE = E2E_PACKAGE + ".util";

    private static final String CLIENT_PACKAGE = "teammates.client";
    private static final String CLIENT_REMOTEAPI_PACKAGE = CLIENT_PACKAGE + ".remoteapi";
    private static final String CLIENT_SCRIPTS_PACKAGE = CLIENT_PACKAGE + ".scripts";
    private static final String CLIENT_UTIL_PACKAGE = CLIENT_PACKAGE + ".util";

    private static String includeSubpackages(String pack) {
        return pack + "..";
    }

    private static JavaClasses forClasses(String... packageNames) {
        return new ClassFileImporter().importPackages(packageNames);
    }

    @Test
    public void testArchitecture_uiShouldNotTouchStorage() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_PACKAGE))
                .check(forClasses(UI_PACKAGE, STORAGE_PACKAGE));
    }

    @Test
    public void testArchitecture_logicShouldNotTouchUi() {
        noClasses().that().resideInAPackage(includeSubpackages(LOGIC_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .check(forClasses(LOGIC_PACKAGE, UI_PACKAGE));
    }

    @Test
    public void testArchitecture_storageShouldNotTouchLogic() {
        noClasses().that().resideInAPackage(includeSubpackages(STORAGE_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_PACKAGE))
                .check(forClasses(STORAGE_PACKAGE, LOGIC_PACKAGE));
    }

    @Test
    public void testArchitecture_storageShouldNotTouchUi() {
        noClasses().that().resideInAPackage(includeSubpackages(STORAGE_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .check(forClasses(STORAGE_PACKAGE, UI_PACKAGE));
    }

    @Test
    public void testArchitecture_commonShouldNotTouchLogic() {
        noClasses().that().resideInAPackage(includeSubpackages(COMMON_PACKAGE))
                // TODO fix these violations
                .and().doNotHaveSimpleName("FeedbackMsqQuestionDetails")
                .should().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_PACKAGE))
                .check(forClasses(COMMON_PACKAGE, LOGIC_PACKAGE));
    }

    @Test
    public void testArchitecture_commonShouldNotTouchStorage() {
        noClasses().that().resideInAPackage(includeSubpackages(COMMON_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_API_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_SEARCH_PACKAGE))
                .check(forClasses(COMMON_PACKAGE, STORAGE_PACKAGE));

        noClasses().that().resideInAPackage(includeSubpackages(COMMON_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(COMMON_PACKAGE + ".datatransfer.attributes"))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .check(forClasses(COMMON_PACKAGE, STORAGE_PACKAGE));
    }

    @Test
    public void testArchitecture_commonShouldNotTouchUi() {
        noClasses().that().resideInAPackage(includeSubpackages(COMMON_PACKAGE))
                // TODO fix this violation
                .and().haveSimpleNameNotEndingWith("QuestionDetails")
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .check(forClasses(COMMON_PACKAGE, UI_PACKAGE));
    }

    @Test
    public void testArchitecture_uiShouldNotTouchLogicExceptForApi() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(LEGACY_UI_CONTROLLER_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_API_PACKAGE))
                .check(forClasses(UI_PACKAGE, LOGIC_PACKAGE));

        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_CORE_PACKAGE))
                .check(forClasses(UI_PACKAGE, LOGIC_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_onlyWebApiCanTouchOutput() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_OUTPUT_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_OUTPUT_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_onlyWebApiCanTouchRequest() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_apiRequestCanOnlyTouchRequestAndOutput() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_apiOutputCanOnlyTouchOutput() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_OUTPUT_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_controllerShouldBeSelfContained() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_automatedActionsShouldBeSelfContained() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_ui_automatedActionsShouldNotTouchOtherUiClasses() {
        noClasses().that().resideInAPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(UI_OUTPUT_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(UI_REQUEST_PACKAGE))
                .check(forClasses(UI_PACKAGE));
    }

    @Test
    public void testArchitecture_logic_logicCanOnlyAccessStorageApi() {
        noClasses().that().resideInAPackage(includeSubpackages(LOGIC_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(LOGIC_CORE_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_PACKAGE))
                .check(forClasses(LOGIC_PACKAGE, STORAGE_PACKAGE));

        noClasses().that().resideInAPackage(includeSubpackages(LOGIC_CORE_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_SEARCH_PACKAGE))
                .check(forClasses(LOGIC_PACKAGE, STORAGE_PACKAGE));
    }

    @Test
    public void testArchitecture_logic_coreLogicCanOnlyAccessItsCorrespondingDb() {
        for (JavaClass coreLogicClass : forClasses(LOGIC_CORE_PACKAGE)) {
            String logicClassName = coreLogicClass.getSimpleName();
            if ("DataBundleLogic".equals(logicClassName)) {
                continue;
            }
            String dbClassName = logicClassName.replace("Logic", "Db");

            noClasses()
                    .that().resideInAPackage(includeSubpackages(LOGIC_CORE_PACKAGE))
                    .and().doNotHaveSimpleName(logicClassName)
                    .and().doNotHaveSimpleName("DataBundleLogic")
                    .should()
                    .accessClassesThat(new DescribedPredicate<JavaClass>("") {
                        @Override
                        public boolean apply(JavaClass input) {
                            return input.getPackageName().startsWith(STORAGE_API_PACKAGE)
                                    && input.getSimpleName().equals(dbClassName);
                        }
                    })
                    .check(forClasses(LOGIC_CORE_PACKAGE, STORAGE_API_PACKAGE));
        }
    }

    @Test
    public void testArchitecture_logic_coreLogicShouldNotTouchApiLogic() {
        noClasses().that().resideInAPackage(includeSubpackages(LOGIC_CORE_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_API_PACKAGE))
                .check(forClasses(LOGIC_PACKAGE));
    }

    @Test
    public void testArchitecture_storage_storageSearchShouldNotTouchStorageEntity() {
        noClasses().that().resideInAPackage(includeSubpackages(STORAGE_SEARCH_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .check(forClasses(STORAGE_PACKAGE));
    }

    @Test
    public void testArchitecture_storage_storageEntityShouldNotTouchOtherStoragePackages() {
        noClasses().that().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return input.getPackageName().startsWith(STORAGE_PACKAGE)
                                && !input.getPackageName().equals(STORAGE_ENTITY_PACKAGE);
                    }
                }).check(forClasses(STORAGE_PACKAGE));
    }

    @Test
    public void testArchitecture_common_assumptionClassCanOnlyBeAccessedByProductionCode() {
        noClasses().that().resideInAPackage(includeSubpackages(TEST_PACKAGE))
                .and().resideInAPackage(includeSubpackages(E2E_PACKAGE))
                .and().resideInAPackage(includeSubpackages(CLIENT_PACKAGE))
                .should().accessClassesThat().haveSimpleName("Assumption")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_testClasses_testCasesShouldBeIndependent() {
        noClasses().that(new DescribedPredicate<JavaClass>("") {
            @Override
            public boolean apply(JavaClass input) {
                return input.getPackageName().startsWith(TEST_CASES_PACKAGE) && !input.isInnerClass();
            }
        }).should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
            @Override
            public boolean apply(JavaClass input) {
                return input.getPackageName().startsWith(TEST_CASES_PACKAGE)
                        && !input.getSimpleName().startsWith("Base")
                        && !input.isInnerClass();
            }
        }).check(forClasses(TEST_CASES_PACKAGE));
    }

    @Test
    public void testArchitecture_testClasses_onlySomeTestClassesCanAccessEntities() {
        noClasses().that().resideInAPackage(includeSubpackages(TEST_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".datatransfer"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".storage"))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .check(forClasses(TEST_PACKAGE, STORAGE_ENTITY_PACKAGE));
    }

    @Test
    public void testArchitecture_testClasses_onlySomeTestClassesCanAccessLogic() {
        noClasses().that().resideInAPackage(includeSubpackages(TEST_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".storage"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".logic"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".action"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".webapi"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".automated"))
                .and().resideOutsideOfPackage(includeSubpackages(TEST_CASES_PACKAGE + ".search"))
                .and().doNotHaveSimpleName("BaseComponentTestCase")
                .and().doNotHaveSimpleName("BaseTestCaseWithMinimalGaeEnvironment")
                .should().accessClassesThat().haveSimpleName("GaeSimulation")
                .orShould().accessClassesThat().haveSimpleName("Logic")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_testClasses_driverShouldNotHaveAnyDependency() {
        noClasses().that().resideInAPackage(includeSubpackages(TEST_DRIVER_PACKAGE))
                .should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return input.getPackageName().startsWith(TEST_PACKAGE)
                                && !input.getPackageName().equals(TEST_DRIVER_PACKAGE);
                    }
                }).check(forClasses(TEST_PACKAGE));
    }

    @Test
    public void testArchitecture_e2e_e2eShouldBeSelfContained() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(E2E_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(LEGACY_PAGEOBJECT_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(LEGACY_BROWSERTESTS_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(E2E_PACKAGE))
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_e2e_e2eShouldNotTouchProductionCodeExceptCommonAndRequests() {
        noClasses().that().resideInAPackage(includeSubpackages(E2E_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(STORAGE_PACKAGE))
                .orShould().accessClassesThat().resideInAPackage(includeSubpackages(LOGIC_PACKAGE))
                .orShould().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return input.getPackageName().startsWith(UI_PACKAGE)
                                && !input.getPackageName().equals(UI_OUTPUT_PACKAGE)
                                && !input.getPackageName().equals(UI_REQUEST_PACKAGE);
                    }
                }).check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_e2e_e2eTestCasesShouldBeIndependentOfEachOther() {
        noClasses().that(new DescribedPredicate<JavaClass>("") {
            @Override
            public boolean apply(JavaClass input) {
                return input.getPackageName().startsWith(E2E_CASES_PACKAGE) && !input.isInnerClass();
            }
        }).should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
            @Override
            public boolean apply(JavaClass input) {
                return input.getPackageName().startsWith(E2E_CASES_PACKAGE)
                        && !input.getSimpleName().startsWith("Base")
                        && !input.isInnerClass();
            }
        }).check(forClasses(E2E_CASES_PACKAGE));
    }

    @Test
    public void testArchitecture_e2e_onlyE2ETestsCanAccessPageObjects() {
        noClasses().that().resideInAPackage(includeSubpackages(E2E_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(E2E_PAGEOBJECTS_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(E2E_CASES_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(E2E_PAGEOBJECTS_PACKAGE))
                .check(forClasses(E2E_PACKAGE));
    }

    @Test
    public void testArchitecture_e2e_utilShouldNotHaveAnyDependency() {
        noClasses().that().resideInAPackage(includeSubpackages(E2E_UTIL_PACKAGE))
                .should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return input.getPackageName().startsWith(E2E_PACKAGE)
                                && !input.getPackageName().equals(E2E_UTIL_PACKAGE);
                    }
                }).check(forClasses(E2E_PACKAGE));
    }

    @Test
    public void testArchitecture_client_clientShouldBeSelfContained() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(CLIENT_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(CLIENT_PACKAGE))
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_client_clientShouldNotTouchUiComponent() {
        noClasses().that().resideInAPackage(includeSubpackages(CLIENT_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(UI_PACKAGE))
                .check(forClasses(CLIENT_PACKAGE, UI_PACKAGE));
    }

    @Test
    public void testArchitecture_client_remoteApiShouldNotTouchScripts() {
        noClasses().that().resideInAPackage(includeSubpackages(CLIENT_REMOTEAPI_PACKAGE))
                .should().accessClassesThat().resideInAPackage(includeSubpackages(CLIENT_SCRIPTS_PACKAGE))
                .check(forClasses(CLIENT_PACKAGE));
    }

    @Test
    public void testArchitecture_client_utilShouldNotHaveAnyDependency() {
        noClasses().that().resideInAPackage(includeSubpackages(CLIENT_UTIL_PACKAGE))
                .should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return input.getPackageName().startsWith(CLIENT_PACKAGE)
                                && !input.getPackageName().equals(CLIENT_UTIL_PACKAGE);
                    }
                }).check(forClasses(CLIENT_PACKAGE));
    }

    @Test
    public void testArchitecture_externalApi_loggingApiCanOnlyBeAccessedByLogger() {
        noClasses().that().doNotHaveSimpleName("Logger")
                .should().accessClassesThat().resideInAPackage("java.util.logging..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_searchApiCanOnlyBeAccessedBySomePackages() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(STORAGE_API_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(STORAGE_SEARCH_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(CLIENT_SCRIPTS_PACKAGE))
                .should().accessClassesThat().resideInAPackage("com.google.appengine.api.search..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_gcsApiCanOnlyBeAccessedByGcsHelper() {
        noClasses().that().doNotHaveSimpleName("GoogleCloudStorageHelper")
                .and().resideOutsideOfPackage(includeSubpackages(CLIENT_SCRIPTS_PACKAGE))
                .should().accessClassesThat().resideInAPackage("com.google.appengine.tools.cloudstorage..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_blobstoreApiCanOnlyBeAccessedByGcsHelper() {
        noClasses().that().doNotHaveSimpleName("GoogleCloudStorageHelper")
                .and().resideOutsideOfPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .should().accessClassesThat().resideInAPackage("com.google.appengine.api.blobstore..")
                .check(ALL_CLASSES);

        noClasses().that().resideInAPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .should().accessClassesThat(new DescribedPredicate<JavaClass>("") {
                    @Override
                    public boolean apply(JavaClass input) {
                        return !"BlobKey".equals(input.getSimpleName())
                                && input.getPackageName().startsWith("com.google.appengine.api.blobstore");
                    }
                }).check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_taskQueueApiCanOnlyBeAccessedByTaskQueueLogic() {
        noClasses().that().doNotHaveSimpleName("TaskQueuesLogic")
                .should().accessClassesThat().resideInAPackage("com.google.appengine.api.taskqueue..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_remoteApiCanOnlyBeAccessedByRemoteApiClient() {
        noClasses().that().doNotHaveSimpleName("RemoteApiClient")
                .should().accessClassesThat().resideInAPackage("com.google.appengine.tools.remoteapi..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_objectifyApiCanOnlyBeAccessedBySomePackages() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(STORAGE_API_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(CLIENT_REMOTEAPI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(CLIENT_SCRIPTS_PACKAGE))
                .and().doNotHaveSimpleName("BaseTestCaseWithObjectifyAccess")
                .should().accessClassesThat().resideInAPackage("com.googlecode.objectify..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_datastoreTypesCanOnlyBeAccessedByEntityClasses() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(STORAGE_ENTITY_PACKAGE))
                .should().accessClassesThat().haveFullyQualifiedName("com.google.appengine.api.datastore.Text")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_servletApiCanOnlyBeAccessedBySomePackages() {
        noClasses().that().doNotHaveSimpleName("ActivityLogEntry")
                .and().doNotHaveSimpleName("GoogleCloudStorageHelper")
                .and().doNotHaveSimpleName("HttpRequestHelper")
                .and().doNotHaveSimpleName("OfyHelper")
                .and().doNotHaveSimpleName("GaeSimulation")
                .and().doNotHaveSimpleName("MockFilterChain")
                .and().doNotHaveSimpleName("MockHttpServletRequest")
                .and().doNotHaveSimpleName("MockHttpServletResponse")
                .and().doNotHaveSimpleName("MockPart")
                .and().resideOutsideOfPackage(includeSubpackages(UI_AUTOMATED_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(UI_WEBAPI_PACKAGE))
                .and().resideOutsideOfPackage(includeSubpackages(LEGACY_UI_CONTROLLER_PACKAGE))
                .should().accessClassesThat().haveFullyQualifiedName("javax.servlet..")
                .check(ALL_CLASSES);
    }

    @Test
    public void testArchitecture_externalApi_assertionApiCanOnlyBeAccessedBySomePackages() {
        noClasses().that().resideOutsideOfPackage(includeSubpackages(E2E_PAGEOBJECTS_PACKAGE))
                .and().doNotHaveSimpleName("BaseTestCase")
                .and().doNotHaveSimpleName("AssertHelper")
                .and().doNotHaveSimpleName("CsvChecker")
                .and().doNotHaveSimpleName("EmailChecker")
                .and().resideOutsideOfPackage(includeSubpackages(LEGACY_PAGEOBJECT_PACKAGE))
                // TODO remove the next after migration
                .and().doNotHaveSimpleName("GaeSimulation")
                .should().accessClassesThat().haveFullyQualifiedName("org.junit.Assert")
                .check(ALL_CLASSES);
    }

}
