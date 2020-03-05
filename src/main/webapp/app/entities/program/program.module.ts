import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { ProgramComponent } from './program.component';
import { ProgramDetailComponent } from './program-detail.component';
import { ProgramUpdateComponent } from './program-update.component';
import { ProgramDeleteDialogComponent } from './program-delete-dialog.component';
import { programRoute } from './program.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(programRoute)],
  declarations: [ProgramComponent, ProgramDetailComponent, ProgramUpdateComponent, ProgramDeleteDialogComponent],
  entryComponents: [ProgramDeleteDialogComponent]
})
export class UgAdmissionProgramModule {}
