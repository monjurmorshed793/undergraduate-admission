import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProgramComponentsPage, ProgramDeleteDialog, ProgramUpdatePage } from './program.page-object';

const expect = chai.expect;

describe('Program e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let programComponentsPage: ProgramComponentsPage;
  let programUpdatePage: ProgramUpdatePage;
  let programDeleteDialog: ProgramDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Programs', async () => {
    await navBarPage.goToEntity('program');
    programComponentsPage = new ProgramComponentsPage();
    await browser.wait(ec.visibilityOf(programComponentsPage.title), 5000);
    expect(await programComponentsPage.getTitle()).to.eq('Programs');
    await browser.wait(ec.or(ec.visibilityOf(programComponentsPage.entities), ec.visibilityOf(programComponentsPage.noResult)), 1000);
  });

  it('should load create Program page', async () => {
    await programComponentsPage.clickOnCreateButton();
    programUpdatePage = new ProgramUpdatePage();
    expect(await programUpdatePage.getPageTitle()).to.eq('Create or edit a Program');
    await programUpdatePage.cancel();
  });

  it('should create and save Programs', async () => {
    const nbButtonsBeforeCreate = await programComponentsPage.countDeleteButtons();

    await programComponentsPage.clickOnCreateButton();

    await promise.all([
      programUpdatePage.setProgramIdInput('5'),
      programUpdatePage.setNameInput('name'),
      programUpdatePage.setCreatedOnInput('2000-12-31'),
      programUpdatePage.setModifiedOnInput('2000-12-31'),
      programUpdatePage.setModifiedByInput('modifiedBy')
    ]);

    expect(await programUpdatePage.getProgramIdInput()).to.eq('5', 'Expected programId value to be equals to 5');
    expect(await programUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await programUpdatePage.getCreatedOnInput()).to.eq('2000-12-31', 'Expected createdOn value to be equals to 2000-12-31');
    expect(await programUpdatePage.getModifiedOnInput()).to.eq('2000-12-31', 'Expected modifiedOn value to be equals to 2000-12-31');
    expect(await programUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');

    await programUpdatePage.save();
    expect(await programUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await programComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Program', async () => {
    const nbButtonsBeforeDelete = await programComponentsPage.countDeleteButtons();
    await programComponentsPage.clickOnLastDeleteButton();

    programDeleteDialog = new ProgramDeleteDialog();
    expect(await programDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Program?');
    await programDeleteDialog.clickOnConfirmButton();

    expect(await programComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
