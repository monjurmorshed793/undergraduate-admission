import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { AdCommitteeComponent } from './ad-committee.component';
import { AdCommitteeDetailComponent } from './ad-committee-detail.component';
import { AdCommitteeUpdateComponent } from './ad-committee-update.component';
import { AdCommitteeDeleteDialogComponent } from './ad-committee-delete-dialog.component';
import { adCommitteeRoute } from './ad-committee.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(adCommitteeRoute)],
  declarations: [AdCommitteeComponent, AdCommitteeDetailComponent, AdCommitteeUpdateComponent, AdCommitteeDeleteDialogComponent],
  entryComponents: [AdCommitteeDeleteDialogComponent]
})
export class UgAdmissionAdCommitteeModule {}
