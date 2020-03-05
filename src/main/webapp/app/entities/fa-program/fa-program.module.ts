import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { FaProgramComponent } from './fa-program.component';
import { FaProgramDetailComponent } from './fa-program-detail.component';
import { FaProgramUpdateComponent } from './fa-program-update.component';
import { FaProgramDeleteDialogComponent } from './fa-program-delete-dialog.component';
import { faProgramRoute } from './fa-program.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(faProgramRoute)],
  declarations: [FaProgramComponent, FaProgramDetailComponent, FaProgramUpdateComponent, FaProgramDeleteDialogComponent],
  entryComponents: [FaProgramDeleteDialogComponent]
})
export class UgAdmissionFaProgramModule {}
