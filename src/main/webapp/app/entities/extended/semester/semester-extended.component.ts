import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISemester } from 'app/shared/model/semester.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SemesterExtendedService } from './semester-extended.service';
import { SemesterExtendedDeleteDialogComponent } from './semester-extended-delete-dialog.component';
import { SemesterComponent } from 'app/entities/semester/semester.component';

@Component({
  selector: 'jhi-semester',
  templateUrl: './semester-extended.component.html'
})
export class SemesterExtendedComponent extends SemesterComponent implements OnInit, OnDestroy {
  constructor(
    protected semesterService: SemesterExtendedService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    super(semesterService, eventManager, modalService, parseLinks);
  }
}
