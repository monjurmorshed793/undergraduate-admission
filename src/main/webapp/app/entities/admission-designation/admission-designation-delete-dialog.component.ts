import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdmissionDesignation } from 'app/shared/model/admission-designation.model';
import { AdmissionDesignationService } from './admission-designation.service';

@Component({
  templateUrl: './admission-designation-delete-dialog.component.html'
})
export class AdmissionDesignationDeleteDialogComponent {
  admissionDesignation?: IAdmissionDesignation;

  constructor(
    protected admissionDesignationService: AdmissionDesignationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.admissionDesignationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('admissionDesignationListModification');
      this.activeModal.close();
    });
  }
}
