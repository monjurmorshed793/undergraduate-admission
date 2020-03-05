import { element, by, ElementFinder } from 'protractor';

export class TotalSeatComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-total-seat div table .btn-danger'));
  title = element.all(by.css('jhi-total-seat div h2#page-heading span')).first();
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

export class TotalSeatUpdatePage {
  pageTitle = element(by.id('jhi-total-seat-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  seatInput = element(by.id('field_seat'));
  createdOnInput = element(by.id('field_createdOn'));
  modifiedOnInput = element(by.id('field_modifiedOn'));
  modifiedByInput = element(by.id('field_modifiedBy'));

  facultyProgramSelect = element(by.id('field_facultyProgram'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setSeatInput(seat: string): Promise<void> {
    await this.seatInput.sendKeys(seat);
  }

  async getSeatInput(): Promise<string> {
    return await this.seatInput.getAttribute('value');
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

  async facultyProgramSelectLastOption(): Promise<void> {
    await this.facultyProgramSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async facultyProgramSelectOption(option: string): Promise<void> {
    await this.facultyProgramSelect.sendKeys(option);
  }

  getFacultyProgramSelect(): ElementFinder {
    return this.facultyProgramSelect;
  }

  async getFacultyProgramSelectedOption(): Promise<string> {
    return await this.facultyProgramSelect.element(by.css('option:checked')).getText();
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

export class TotalSeatDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-totalSeat-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-totalSeat'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
