import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISemester } from 'app/shared/model/semester.model';
import { SemesterExtendedService } from './semester-extended.service';
import { SemesterDeleteDialogComponent } from 'app/entities/semester/semester-delete-dialog.component';

@Component({
  templateUrl: './semester-extended-delete-dialog.component.html'
})
export class SemesterExtendedDeleteDialogComponent extends SemesterDeleteDialogComponent {
  constructor(
    protected semesterService: SemesterExtendedService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {
    super(semesterService, activeModal, eventManager);
  }
}
