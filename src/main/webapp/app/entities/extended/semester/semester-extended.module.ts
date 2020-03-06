import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { SemesterExtendedComponent } from './semester-extended.component';
import { SemesterExtendedDetailComponent } from './semester-extended-detail.component';
import { SemesterExtendedUpdateComponent } from './semester-extended-update.component';
import { SemesterExtendedDeleteDialogComponent } from './semester-extended-delete-dialog.component';
import { semesterExtendedRoute } from './semester-extended.route';
import { SemesterComponent } from 'app/entities/semester/semester.component';
import { SemesterUpdateComponent } from 'app/entities/semester/semester-update.component';
import { SemesterDetailComponent } from 'app/entities/semester/semester-detail.component';
import { SemesterDeleteDialogComponent } from 'app/entities/semester/semester-delete-dialog.component';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(semesterExtendedRoute)],
  declarations: [
    SemesterComponent,
    SemesterUpdateComponent,
    SemesterDetailComponent,
    SemesterDeleteDialogComponent,
    SemesterExtendedComponent,
    SemesterExtendedDetailComponent,
    SemesterExtendedUpdateComponent,
    SemesterExtendedDeleteDialogComponent
  ],
  entryComponents: [SemesterExtendedDeleteDialogComponent]
})
export class UgAdmissionSemesterExtendedModule {}
