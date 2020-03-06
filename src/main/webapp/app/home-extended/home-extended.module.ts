import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeExtendedComponent } from './home-extended.component';

@NgModule({
  imports: [UgAdmissionSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeExtendedComponent]
})
export class UgAdmissionHomeExtendedModule {}
