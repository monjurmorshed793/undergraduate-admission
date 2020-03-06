import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { SemesterComponent } from './semester.component';
import { SemesterDetailComponent } from './semester-detail.component';
import { SemesterUpdateComponent } from './semester-update.component';
import { SemesterDeleteDialogComponent } from './semester-delete-dialog.component';
import { semesterRoute } from './semester.route';

@NgModule({
  imports: [
    UgAdmissionSharedModule,
    RouterModule.forChild(semesterRoute)
  ] /*,
  declarations: [SemesterComponent, SemesterDetailComponent, SemesterUpdateComponent, SemesterDeleteDialogComponent],
  entryComponents: [SemesterDeleteDialogComponent]*/
})
export class UgAdmissionSemesterModule {}
