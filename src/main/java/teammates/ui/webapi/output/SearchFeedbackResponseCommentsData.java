package teammates.ui.webapi.output;

import java.util.List;
import java.util.Map;

import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseCommentAttributes;

/**
 * Output for Feedback Response, Comments, and Questions search.
 */
public class SearchFeedbackResponseCommentsData extends ApiOutput {
    private final Map<String, List<FeedbackResponseCommentAttributes>> comments;
    private final Map<String, List<FeedbackResponseAttributes>> responses;
    private final Map<String, List<FeedbackQuestionAttributes>> questions;

    public SearchFeedbackResponseCommentsData(
            Map<String, List<FeedbackResponseCommentAttributes>> comments,
            Map<String, List<FeedbackResponseAttributes>> responses,
            Map<String, List<FeedbackQuestionAttributes>> questions
    ) {
        this.comments = comments;
        this.responses = responses;
        this.questions = questions;
    }

    public Map<String, List<FeedbackResponseCommentAttributes>> getComments() {
        return comments;
    }

    public Map<String, List<FeedbackResponseAttributes>> getResponses() {
        return responses;
    }

    public Map<String, List<FeedbackQuestionAttributes>> getQuestions() {
        return questions;
    }
}
