package teammates.ui.webapi.action;

import java.util.List;

import teammates.common.datatransfer.FeedbackResponseCommentSearchResultBundle;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.exception.UnauthorizedAccessException;
import teammates.common.util.Const;
import teammates.ui.webapi.output.SearchFeedbackResponseCommentsData;

/**
 * Action: Searches for FeedbackResponseComments.
 */
public class SearchFeedbackResponseCommentsAction extends Action {
    @Override
    protected AuthType getMinAuthLevel() {
        return AuthType.LOGGED_IN;
    }

    @Override
    public void checkSpecificAccessControl() {
        // Only instructor can search
        if (!userInfo.isInstructor) {
            throw new UnauthorizedAccessException("Instructor privilege is required to access this resource.");
        }
    }

    @Override
    public ActionResult execute() {
        String searchKey = getNonNullRequestParamValue(Const.ParamsNames.SEARCH_KEY);
        List<InstructorAttributes> instructors = logic.getInstructorsForGoogleId(userInfo.id);
        FeedbackResponseCommentSearchResultBundle frCommentSearchResults =
                logic.searchFeedbackResponseComments(searchKey, instructors);

        return new JsonResult(new SearchFeedbackResponseCommentsData(
                frCommentSearchResults.comments,
                frCommentSearchResults.responses,
                frCommentSearchResults.questions
        ));
    }
}
