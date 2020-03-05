import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TotalSeatComponentsPage, TotalSeatDeleteDialog, TotalSeatUpdatePage } from './total-seat.page-object';

const expect = chai.expect;

describe('TotalSeat e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let totalSeatComponentsPage: TotalSeatComponentsPage;
  let totalSeatUpdatePage: TotalSeatUpdatePage;
  let totalSeatDeleteDialog: TotalSeatDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TotalSeats', async () => {
    await navBarPage.goToEntity('total-seat');
    totalSeatComponentsPage = new TotalSeatComponentsPage();
    await browser.wait(ec.visibilityOf(totalSeatComponentsPage.title), 5000);
    expect(await totalSeatComponentsPage.getTitle()).to.eq('Total Seats');
    await browser.wait(ec.or(ec.visibilityOf(totalSeatComponentsPage.entities), ec.visibilityOf(totalSeatComponentsPage.noResult)), 1000);
  });

  it('should load create TotalSeat page', async () => {
    await totalSeatComponentsPage.clickOnCreateButton();
    totalSeatUpdatePage = new TotalSeatUpdatePage();
    expect(await totalSeatUpdatePage.getPageTitle()).to.eq('Create or edit a Total Seat');
    await totalSeatUpdatePage.cancel();
  });

  it('should create and save TotalSeats', async () => {
    const nbButtonsBeforeCreate = await totalSeatComponentsPage.countDeleteButtons();

    await totalSeatComponentsPage.clickOnCreateButton();

    await promise.all([
      totalSeatUpdatePage.setSeatInput('5'),
      totalSeatUpdatePage.setCreatedOnInput('2000-12-31'),
      totalSeatUpdatePage.setModifiedOnInput('2000-12-31'),
      totalSeatUpdatePage.setModifiedByInput('modifiedBy'),
      totalSeatUpdatePage.facultyProgramSelectLastOption()
    ]);

    expect(await totalSeatUpdatePage.getSeatInput()).to.eq('5', 'Expected seat value to be equals to 5');
    expect(await totalSeatUpdatePage.getCreatedOnInput()).to.eq('2000-12-31', 'Expected createdOn value to be equals to 2000-12-31');
    expect(await totalSeatUpdatePage.getModifiedOnInput()).to.eq('2000-12-31', 'Expected modifiedOn value to be equals to 2000-12-31');
    expect(await totalSeatUpdatePage.getModifiedByInput()).to.eq('modifiedBy', 'Expected ModifiedBy value to be equals to modifiedBy');

    await totalSeatUpdatePage.save();
    expect(await totalSeatUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await totalSeatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TotalSeat', async () => {
    const nbButtonsBeforeDelete = await totalSeatComponentsPage.countDeleteButtons();
    await totalSeatComponentsPage.clickOnLastDeleteButton();

    totalSeatDeleteDialog = new TotalSeatDeleteDialog();
    expect(await totalSeatDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Total Seat?');
    await totalSeatDeleteDialog.clickOnConfirmButton();

    expect(await totalSeatComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
