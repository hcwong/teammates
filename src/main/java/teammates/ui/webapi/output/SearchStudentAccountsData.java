package teammates.ui.webapi.output;

import java.util.List;

import teammates.common.datatransfer.StudentAccountSearchResult;

/**
 * Output format for student search results.
 */
public class SearchStudentAccountsData extends ApiOutput {
    private final List<StudentAccountSearchResult> students;

    public SearchStudentAccountsData(List<StudentAccountSearchResult> students) {
        this.students = students;
    }

    public List<StudentAccountSearchResult> getStudents() {
        return students;
    }
}
