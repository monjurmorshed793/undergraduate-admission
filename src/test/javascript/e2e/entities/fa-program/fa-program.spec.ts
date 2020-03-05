import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  FaProgramComponentsPage,
  /* FaProgramDeleteDialog, */
  FaProgramUpdatePage
} from './fa-program.page-object';

const expect = chai.expect;

describe('FaProgram e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let faProgramComponentsPage: FaProgramComponentsPage;
  let faProgramUpdatePage: FaProgramUpdatePage;
  /* let faProgramDeleteDialog: FaProgramDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FaPrograms', async () => {
    await navBarPage.goToEntity('fa-program');
    faProgramComponentsPage = new FaProgramComponentsPage();
    await browser.wait(ec.visibilityOf(faProgramComponentsPage.title), 5000);
    expect(await faProgramComponentsPage.getTitle()).to.eq('Fa Programs');
    await browser.wait(ec.or(ec.visibilityOf(faProgramComponentsPage.entities), ec.visibilityOf(faProgramComponentsPage.noResult)), 1000);
  });

  it('should load create FaProgram page', async () => {
    await faProgramComponentsPage.clickOnCreateButton();
    faProgramUpdatePage = new FaProgramUpdatePage();
    expect(await faProgramUpdatePage.getPageTitle()).to.eq('Create or edit a Fa Program');
    await faProgramUpdatePage.cancel();
  });

  /* it('should create and save FaPrograms', async () => {
        const nbButtonsBeforeCreate = await faProgramComponentsPage.countDeleteButtons();

        await faProgramComponentsPage.clickOnCreateButton();

        await promise.all([
            faProgramUpdatePage.setCreatedOnInput('2000-12-31'),
            faProgramUpdatePage.setModifiedOnInput('2000-12-31'),
            faProgramUpdatePage.setModifiedByInput('modifiedBy'),
            faProgramUpdatePage.semesterSelectLastOption(),
            faProgramUpdatePage.facultySelectLastOption(),
            faProgramUpdatePage.programSelectLastOption(),
        ]);

        expect(await faProgramUpdatePage.getCreatedOnInput()).to.eq('2000-12-31', 'Expected createdOn value to be equals to 2000-12-31');
        expect(await faProgramUpdatePage.getModifiedOnInput()).to.eq('2000-12-31', 'Expected modifiedOn value to be equals to 2000-12-31');
        expect(await faProgramUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');

        await faProgramUpdatePage.save();
        expect(await faProgramUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await faProgramComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last FaProgram', async () => {
        const nbButtonsBeforeDelete = await faProgramComponentsPage.countDeleteButtons();
        await faProgramComponentsPage.clickOnLastDeleteButton();

        faProgramDeleteDialog = new FaProgramDeleteDialog();
        expect(await faProgramDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Fa Program?');
        await faProgramDeleteDialog.clickOnConfirmButton();

        expect(await faProgramComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
