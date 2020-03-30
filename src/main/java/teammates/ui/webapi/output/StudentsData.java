package teammates.ui.webapi.output;

import java.util.List;
import java.util.stream.Collectors;

import teammates.common.datatransfer.attributes.StudentAttributes;

/**
 * The API output format of a list of {@link StudentAttributes}.
 */
public class StudentsData extends ApiOutput {

    private List<StudentData> students;

    public StudentsData() {
        // Use this constructor if StudentData is to be sent with the key
    }

    public StudentsData(List<StudentAttributes> students) {
        this.students = students.stream().map(StudentData::new).collect(Collectors.toList());
    }

    public List<StudentData> getStudents() {
        return students;
    }

    public void setStudents(List<StudentData> students) {
        this.students = students;
    }
}
