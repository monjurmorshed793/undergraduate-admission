import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { TotalSeatComponent } from './total-seat.component';
import { TotalSeatDetailComponent } from './total-seat-detail.component';
import { TotalSeatUpdateComponent } from './total-seat-update.component';
import { TotalSeatDeleteDialogComponent } from './total-seat-delete-dialog.component';
import { totalSeatRoute } from './total-seat.route';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild(totalSeatRoute)],
  declarations: [TotalSeatComponent, TotalSeatDetailComponent, TotalSeatUpdateComponent, TotalSeatDeleteDialogComponent],
  entryComponents: [TotalSeatDeleteDialogComponent]
})
export class UgAdmissionTotalSeatModule {}
