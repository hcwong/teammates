package teammates.ui.webapi.action;

import java.util.List;

import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.ui.webapi.output.SearchStudentsData;

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
        String searchKey = getNonNullRequestParamValue(Const.ParamsNames.ADMIN_SEARCH_KEY);
        List<InstructorAttributes> instructors = logic.getInstructorsForGoogleId(userInfo.id);

        List<StudentAttributes> students = logic.searchStudents(searchKey, instructors).studentList;
        return new JsonResult(new SearchStudentsData(students));
    }
}