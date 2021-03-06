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
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { FooterExtendedComponent } from 'app/layouts/footer/footer-extended.component';
import { UgAdmissionHomeExtendedModule } from 'app/home-extended/home-extended.module';
import { NavbarExtendedComponent } from 'app/layouts/navbar-extended/navbar-extended.component';

@NgModule({
  imports: [
    BrowserModule,
    UgAdmissionSharedModule,
    UgAdmissionCoreModule,
    UgAdmissionHomeExtendedModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    UgAdmissionEntityModule,
    UgAdmissionAppRoutingModule
  ],
  declarations: [MainComponent, NavbarExtendedComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterExtendedComponent],
  bootstrap: [MainComponent]
})
export class UgAdmissionAppModule {}
