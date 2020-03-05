import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AdCommitteeComponentsPage, AdCommitteeDeleteDialog, AdCommitteeUpdatePage } from './ad-committee.page-object';

const expect = chai.expect;

describe('AdCommittee e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let adCommitteeComponentsPage: AdCommitteeComponentsPage;
  let adCommitteeUpdatePage: AdCommitteeUpdatePage;
  let adCommitteeDeleteDialog: AdCommitteeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AdCommittees', async () => {
    await navBarPage.goToEntity('ad-committee');
    adCommitteeComponentsPage = new AdCommitteeComponentsPage();
    await browser.wait(ec.visibilityOf(adCommitteeComponentsPage.title), 5000);
    expect(await adCommitteeComponentsPage.getTitle()).to.eq('Ad Committees');
    await browser.wait(
      ec.or(ec.visibilityOf(adCommitteeComponentsPage.entities), ec.visibilityOf(adCommitteeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AdCommittee page', async () => {
    await adCommitteeComponentsPage.clickOnCreateButton();
    adCommitteeUpdatePage = new AdCommitteeUpdatePage();
    expect(await adCommitteeUpdatePage.getPageTitle()).to.eq('Create or edit a Ad Committee');
    await adCommitteeUpdatePage.cancel();
  });

  it('should create and save AdCommittees', async () => {
    const nbButtonsBeforeCreate = await adCommitteeComponentsPage.countDeleteButtons();

    await adCommitteeComponentsPage.clickOnCreateButton();

    await promise.all([
      adCommitteeUpdatePage.setCreatedOnInput('2000-12-31'),
      adCommitteeUpdatePage.setModifiedOnInput('2000-12-31'),
      adCommitteeUpdatePage.setModifiedByInput('modifiedBy'),
      adCommitteeUpdatePage.semesterSelectLastOption(),
      adCommitteeUpdatePage.facultySelectLastOption(),
      adCommitteeUpdatePage.designationSelectLastOption(),
      adCommitteeUpdatePage.userSelectLastOption()
    ]);

    expect(await adCommitteeUpdatePage.getCreatedOnInput()).to.eq('2000-12-31', 'Expected createdOn value to be equals to 2000-12-31');
    expect(await adCommitteeUpdatePage.getModifiedOnInput()).to.eq('2000-12-31', 'Expected modifiedOn value to be equals to 2000-12-31');
    expect(await adCommitteeUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');

    await adCommitteeUpdatePage.save();
    expect(await adCommitteeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await adCommitteeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last AdCommittee', async () => {
    const nbButtonsBeforeDelete = await adCommitteeComponentsPage.countDeleteButtons();
    await adCommitteeComponentsPage.clickOnLastDeleteButton();

    adCommitteeDeleteDialog = new AdCommitteeDeleteDialog();
    expect(await adCommitteeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Ad Committee?');
    await adCommitteeDeleteDialog.clickOnConfirmButton();

    expect(await adCommitteeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
