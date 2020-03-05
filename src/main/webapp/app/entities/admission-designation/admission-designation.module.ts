import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { AdmissionDesignationComponent } from './admission-designation.component';
import { AdmissionDesignationDetailComponent } from './admission-designation-detail.component';
import { AdmissionDesignationUpdateComponent } from './admission-designation-update.component';
import { AdmissionDesignationDeleteDialogComponent } from './admission-designation-delete-dialog.component';
import { admissionDesignationRoute } from './admission-designation.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(admissionDesignationRoute)],
  declarations: [
    AdmissionDesignationComponent,
    AdmissionDesignationDetailComponent,
    AdmissionDesignationUpdateComponent,
    AdmissionDesignationDeleteDialogComponent
  ],
  entryComponents: [AdmissionDesignationDeleteDialogComponent]
})
export class UgAdmissionAdmissionDesignationModule {}
