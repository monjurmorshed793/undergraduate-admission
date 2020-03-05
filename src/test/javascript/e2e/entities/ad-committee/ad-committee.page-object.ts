import { element, by, ElementFinder } from 'protractor';

export class AdCommitteeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ad-committee div table .btn-danger'));
  title = element.all(by.css('jhi-ad-committee div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class AdCommitteeUpdatePage {
  pageTitle = element(by.id('jhi-ad-committee-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  createdOnInput = element(by.id('field_createdOn'));
  modifiedOnInput = element(by.id('field_modifiedOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));

  semesterSelect = element(by.id('field_semester'));
  facultySelect = element(by.id('field_faculty'));
  designationSelect = element(by.id('field_designation'));
  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setCreatedOnInput(createdOn: string): Promise<void> {
    await this.createdOnInput.sendKeys(createdOn);
  }

  async getCreatedOnInput(): Promise<string> {
    return await this.createdOnInput.getAttribute('value');
  }

  async setModifiedOnInput(modifiedOn: string): Promise<void> {
    await this.modifiedOnInput.sendKeys(modifiedOn);
  }

  async getModifiedOnInput(): Promise<string> {
    return await this.modifiedOnInput.getAttribute('value');
  }

  async setModifiedByInput(modifiedBy: string): Promise<void> {
    await this.modifiedByInput.sendKeys(modifiedBy);
  }

  async getModifiedByInput(): Promise<string> {
    return await this.modifiedByInput.getAttribute('value');
  }

  async semesterSelectLastOption(): Promise<void> {
    await this.semesterSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async semesterSelectOption(option: string): Promise<void> {
    await this.semesterSelect.sendKeys(option);
  }

  getSemesterSelect(): ElementFinder {
    return this.semesterSelect;
  }

  async getSemesterSelectedOption(): Promise<string> {
    return await this.semesterSelect.element(by.css('option:checked')).getText();
  }

  async facultySelectLastOption(): Promise<void> {
    await this.facultySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async facultySelectOption(option: string): Promise<void> {
    await this.facultySelect.sendKeys(option);
  }

  getFacultySelect(): ElementFinder {
    return this.facultySelect;
  }

  async getFacultySelectedOption(): Promise<string> {
    return await this.facultySelect.element(by.css('option:checked')).getText();
  }

  async designationSelectLastOption(): Promise<void> {
    await this.designationSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async designationSelectOption(option: string): Promise<void> {
    await this.designationSelect.sendKeys(option);
  }

  getDesignationSelect(): ElementFinder {
    return this.designationSelect;
  }

  async getDesignationSelectedOption(): Promise<string> {
    return await this.designationSelect.element(by.css('option:checked')).getText();
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class AdCommitteeDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-adCommittee-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-adCommittee'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
