import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FacultyComponentsPage, FacultyDeleteDialog, FacultyUpdatePage } from './faculty.page-object';

const expect = chai.expect;

describe('Faculty e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let facultyComponentsPage: FacultyComponentsPage;
  let facultyUpdatePage: FacultyUpdatePage;
  let facultyDeleteDialog: FacultyDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Faculties', async () => {
    await navBarPage.goToEntity('faculty');
    facultyComponentsPage = new FacultyComponentsPage();
    await browser.wait(ec.visibilityOf(facultyComponentsPage.title), 5000);
    expect(await facultyComponentsPage.getTitle()).to.eq('Faculties');
    await browser.wait(ec.or(ec.visibilityOf(facultyComponentsPage.entities), ec.visibilityOf(facultyComponentsPage.noResult)), 1000);
  });

  it('should load create Faculty page', async () => {
    await facultyComponentsPage.clickOnCreateButton();
    facultyUpdatePage = new FacultyUpdatePage();
    expect(await facultyUpdatePage.getPageTitle()).to.eq('Create or edit a Faculty');
    await facultyUpdatePage.cancel();
  });

  it('should create and save Faculties', async () => {
    const nbButtonsBeforeCreate = await facultyComponentsPage.countDeleteButtons();

    await facultyComponentsPage.clickOnCreateButton();

    await promise.all([
      facultyUpdatePage.setNameInput('name'),
      facultyUpdatePage.setShortNameInput('shortName'),
      facultyUpdatePage.setCreatedOnInput('2000-12-31'),
      facultyUpdatePage.setModifiedOnInput('2000-12-31'),
      facultyUpdatePage.setModifiedByInput('modifiedBy')
    ]);

    expect(await facultyUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await facultyUpdatePage.getShortNameInput()).to.eq('shortName', 'Expected ShortName value to be equals to shortName');
    expect(await facultyUpdatePage.getCreatedOnInput()).to.eq('2000-12-31', 'Expected createdOn value to be equals to 2000-12-31');
    expect(await facultyUpdatePage.getModifiedOnInput()).to.eq('2000-12-31', 'Expected modifiedOn value to be equals to 2000-12-31');
    expect(await facultyUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');

    await facultyUpdatePage.save();
    expect(await facultyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await facultyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Faculty', async () => {
    const nbButtonsBeforeDelete = await facultyComponentsPage.countDeleteButtons();
    await facultyComponentsPage.clickOnLastDeleteButton();

    facultyDeleteDialog = new FacultyDeleteDialog();
    expect(await facultyDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Faculty?');
    await facultyDeleteDialog.clickOnConfirmButton();

    expect(await facultyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
