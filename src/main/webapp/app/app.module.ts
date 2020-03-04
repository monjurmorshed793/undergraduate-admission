import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { UgAdmissionSharedModule } from 'app/shared/shared.module';
import { UgAdmissionCoreModule } from 'app/core/core.module';
import { UgAdmissionAppRoutingModule } from './app-routing.module';
import { UgAdmissionHomeModule } from './home/home.module';
import { UgAdmissionEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    UgAdmissionSharedModule,
    UgAdmissionCoreModule,
    UgAdmissionHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    UgAdmissionEntityModule,
    UgAdmissionAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class UgAdmissionAppModule {}
