import { element, by, ElementFinder } from 'protractor';

export class FaProgramComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-fa-program div table .btn-danger'));
  title = element.all(by.css('jhi-fa-program div h2#page-heading span')).first();
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

export class FaProgramUpdatePage {
  pageTitle = element(by.id('jhi-fa-program-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  createdOnInput = element(by.id('field_createdOn'));
  modifiedOnInput = element(by.id('field_modifiedOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));

  semesterSelect = element(by.id('field_semester'));
  facultySelect = element(by.id('field_faculty'));
  programSelect = element(by.id('field_program'));

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

  async programSelectLastOption(): Promise<void> {
    await this.programSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async programSelectOption(option: string): Promise<void> {
    await this.programSelect.sendKeys(option);
  }

  getProgramSelect(): ElementFinder {
    return this.programSelect;
  }

  async getProgramSelectedOption(): Promise<string> {
    return await this.programSelect.element(by.css('option:checked')).getText();
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

export class FaProgramDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-faProgram-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-faProgram'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
