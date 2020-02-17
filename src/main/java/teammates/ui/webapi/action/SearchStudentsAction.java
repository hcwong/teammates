package teammates.ui.webapi.action;

import java.util.List;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.ui.webapi.output.SearchStudentsData;

/**
 * Action for searching for students.
 */
public class SearchStudentsAction extends Action {
    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.LOGGED_IN;
    }

    @Override
    public void checkSpecificAccessControl() {
        // Only instructors and admins can search for student
        if (userInfo.isStudent) {
            throw new UnauthorizedAccessException("Instructor or Admin privilege is required to access this resource.");
        }
    }

    @Override
    public ActionResult execute() {
        String searchKey = getNonNullRequestParamValue(Const.ParamsNames.SEARCH_KEY);
        List<StudentAttributes> students;
        if (userInfo.isAdmin) {
            students = logic.searchStudentsInWholeSystem(searchKey).studentList;
        } else {
            List<InstructorAttributes> instructors = logic.getInstructorsForGoogleId(userInfo.id);
            students = logic.searchStudents(searchKey, instructors).studentList;
        }
        return new JsonResult(new SearchStudentsData(students));
    }
}
