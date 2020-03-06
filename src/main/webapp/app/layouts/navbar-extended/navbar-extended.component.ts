import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { LoginService } from 'app/core/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { NavbarComponent } from 'app/layouts/navbar/navbar.component';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar-extended.component.html',
  styleUrls: ['navbar-extended.scss']
})
export class NavbarExtendedComponent extends NavbarComponent implements OnInit {
  constructor(
    loginService: LoginService,
    accountService: AccountService,
    loginModalService: LoginModalService,
    profileService: ProfileService,
    router: Router
  ) {
    super(loginService, accountService, loginModalService, profileService, router);
  }
}
