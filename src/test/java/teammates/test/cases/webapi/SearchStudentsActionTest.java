package teammates.test.cases.webapi;

import org.junit.Test;
import teammates.common.datatransfer.DataBundle;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.util.Const;
import teammates.ui.webapi.action.JsonResult;
import teammates.ui.webapi.action.SearchAccountsAction;
import teammates.ui.webapi.action.SearchStudentsAction;
import teammates.ui.webapi.output.AdminSearchResultData;
import teammates.ui.webapi.output.SearchStudentsData;

/**
 * SUT:{@link SearchStudentsAction}
 */
public class SearchStudentsActionTest extends BaseActionTest<SearchStudentsAction> {

    @Override
    protected void prepareTestData() {
        DataBundle dataBundle = getTypicalDataBundle();
        removeAndRestoreDataBundle(dataBundle);
        putDocuments(dataBundle);
    }

    @Override
    protected String getActionUri() {
        return Const.ResourceURIs.STUDENTS_SEARCH;
    }

    @Override
    protected String getRequestMethod() {
        return GET;
    }

    @Override
    @Test
    protected void testExecute() {
        InstructorAttributes acc = typicalBundle.instructors.get("instructor1OfCourse1");
        loginAsAdmin();

        ______TS("Not enough parameters");

        verifyHttpParameterFailure();

        ______TS("Typical case (Admin): search google id");

        String[] googleIdParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, acc.getGoogleId(),
        };

        SearchStudentsAction a = getAction(googleIdParams);
        JsonResult result = getJsonResult(a);
        SearchStudentsData response = (SearchStudentsData) result.getOutput();
        assertTrue(response.getStudents().isEmpty());

        ______TS("Typical case (Admin): search course id");
        String[] courseIdParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, acc.getCourseId(),
        };

        a = getAction(courseIdParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(5, response.getStudents().size());

        ______TS("Typical case (Admin): full text search accounts that contains 'Course2'");
        String[] accNameParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "Course2",
        };

        a = getAction(accNameParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(2, response.getStudents().size());

        ______TS("Typical case (Admin): search email");
        String[] emailParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "@course1.tmt",
        };

        a = getAction(emailParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(0, response.getStudents().size());

        ______TS("Typical case (Admin): search has no match");
        String[] noMatchParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "nomatch",
        };

        a = getAction(noMatchParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(0, response.getStudents().size());

        // Instructor tests below
        loginAsInstructor(acc.getGoogleId());

        ______TS("Typical case (Instructor): search google id");

        googleIdParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, acc.getGoogleId(),
        };

        a = getAction(googleIdParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();
        assertTrue(response.getStudents().isEmpty());

        ______TS("Typical case (Instructor): search course id");
        courseIdParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, acc.getCourseId(),
        };

        a = getAction(courseIdParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(5, response.getStudents().size());

        ______TS("Typical case (Instructor): full text search accounts that contains 'Course2'");
        accNameParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "Course2",
        };

        a = getAction(accNameParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(2, response.getStudents().size());

        ______TS("Typical case (Instructor): search email");
        emailParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "@course1.tmt",
        };

        a = getAction(emailParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(0, response.getStudents().size());

        ______TS("Typical case (Instructor): search has no match");
        noMatchParams = new String[] {
                Const.ParamsNames.SEARCH_KEY, "nomatch",
        };

        a = getAction(noMatchParams);
        result = getJsonResult(a);
        response = (SearchStudentsData) result.getOutput();

        assertEquals(0, response.getStudents().size());
    }

    @Override
    @Test
    protected void testAccessControl() {
        verifyAccessibleForAdmin();
        verifyOnlyInstructorsCanAccess();
    }
}
