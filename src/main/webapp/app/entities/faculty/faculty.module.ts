import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { FacultyComponent } from './faculty.component';
import { FacultyDetailComponent } from './faculty-detail.component';
import { FacultyUpdateComponent } from './faculty-update.component';
import { FacultyDeleteDialogComponent } from './faculty-delete-dialog.component';
import { facultyRoute } from './faculty.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(facultyRoute)],
  declarations: [FacultyComponent, FacultyDetailComponent, FacultyUpdateComponent, FacultyDeleteDialogComponent],
  entryComponents: [FacultyDeleteDialogComponent]
})
export class UgAdmissionFacultyModule {}
