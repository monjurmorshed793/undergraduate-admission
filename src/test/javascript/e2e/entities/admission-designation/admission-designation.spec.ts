import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  AdmissionDesignationComponentsPage,
  AdmissionDesignationDeleteDialog,
  AdmissionDesignationUpdatePage
} from './admission-designation.page-object';

const expect = chai.expect;

describe('AdmissionDesignation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let admissionDesignationComponentsPage: AdmissionDesignationComponentsPage;
  let admissionDesignationUpdatePage: AdmissionDesignationUpdatePage;
  let admissionDesignationDeleteDialog: AdmissionDesignationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AdmissionDesignations', async () => {
    await navBarPage.goToEntity('admission-designation');
    admissionDesignationComponentsPage = new AdmissionDesignationComponentsPage();
    await browser.wait(ec.visibilityOf(admissionDesignationComponentsPage.title), 5000);
    expect(await admissionDesignationComponentsPage.getTitle()).to.eq('Admission Designations');
    await browser.wait(
      ec.or(ec.visibilityOf(admissionDesignationComponentsPage.entities), ec.visibilityOf(admissionDesignationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AdmissionDesignation page', async () => {
    await admissionDesignationComponentsPage.clickOnCreateButton();
    admissionDesignationUpdatePage = new AdmissionDesignationUpdatePage();
    expect(await admissionDesignationUpdatePage.getPageTitle()).to.eq('Create or edit a Admission Designation');
    await admissionDesignationUpdatePage.cancel();
  });

  it('should create and save AdmissionDesignations', async () => {
    const nbButtonsBeforeCreate = await admissionDesignationComponentsPage.countDeleteButtons();

    await admissionDesignationComponentsPage.clickOnCreateButton();

    await promise.all([
      admissionDesignationUpdatePage.setNameInput('name'),
      admissionDesignationUpdatePage.setDescriptionInput('description'),
      admissionDesignationUpdatePage.setCreatedOnInput('2000-12-31'),
      admissionDesignationUpdatePage.setModifiedOnInput('2000-12-31'),
      admissionDesignationUpdatePage.setModifiedByInput('modifiedBy')
    ]);

    expect(await admissionDesignationUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await admissionDesignationUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await admissionDesignationUpdatePage.getCreatedOnInput()).to.eq(
      '2000-12-31',
      'Expected createdOn value to be equals to 2000-12-31'
    );
    expect(await admissionDesignationUpdatePage.getModifiedOnInput()).to.eq(
      '2000-12-31',
      'Expected modifiedOn value to be equals to 2000-12-31'
    );
    expect(await admissionDesignationUpdatePage.getModifiedByInput()).to.eq(
      'modifiedBy',
      'Expected ModifiedBy value to be equals to modifiedBy'
    );

    await admissionDesignationUpdatePage.save();
    expect(await admissionDesignationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await admissionDesignationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AdmissionDesignation', async () => {
    const nbButtonsBeforeDelete = await admissionDesignationComponentsPage.countDeleteButtons();
    await admissionDesignationComponentsPage.clickOnLastDeleteButton();

    admissionDesignationDeleteDialog = new AdmissionDesignationDeleteDialog();
    expect(await admissionDesignationDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Admission Designation?');
    await admissionDesignationDeleteDialog.clickOnConfirmButton();

    expect(await admissionDesignationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
