package jp.co.sample.emp_management.form;

public class SearchEmployeeForm {
	private String name;

	public SearchEmployeeForm() {}

	public SearchEmployeeForm(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
