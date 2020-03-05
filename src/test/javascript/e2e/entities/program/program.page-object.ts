import { element, by, ElementFinder } from 'protractor';

export class ProgramComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-program div table .btn-danger'));
  title = element.all(by.css('jhi-program div h2#page-heading span')).first();
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

export class ProgramUpdatePage {
  pageTitle = element(by.id('jhi-program-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  programIdInput = element(by.id('field_programId'));
  nameInput = element(by.id('field_name'));
  createdOnInput = element(by.id('field_createdOn'));
  modifiedOnInput = element(by.id('field_modifiedOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setProgramIdInput(programId: string): Promise<void> {
    await this.programIdInput.sendKeys(programId);
  }

  async getProgramIdInput(): Promise<string> {
    return await this.programIdInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
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

export class ProgramDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-program-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-program'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
