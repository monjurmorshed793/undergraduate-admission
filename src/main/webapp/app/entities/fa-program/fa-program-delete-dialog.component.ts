import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFaProgram } from 'app/shared/model/fa-program.model';
import { FaProgramService } from './fa-program.service';

@Component({
  templateUrl: './fa-program-delete-dialog.component.html'
})
export class FaProgramDeleteDialogComponent {
  faProgram?: IFaProgram;

  constructor(protected faProgramService: FaProgramService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.faProgramService.delete(id).subscribe(() => {
      this.eventManager.broadcast('faProgramListModification');
      this.activeModal.close();
    });
  }
}
