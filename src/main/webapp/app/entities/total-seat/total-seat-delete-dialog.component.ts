import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITotalSeat } from 'app/shared/model/total-seat.model';
import { TotalSeatService } from './total-seat.service';

@Component({
  templateUrl: './total-seat-delete-dialog.component.html'
})
export class TotalSeatDeleteDialogComponent {
  totalSeat?: ITotalSeat;

  constructor(protected totalSeatService: TotalSeatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.totalSeatService.delete(id).subscribe(() => {
      this.eventManager.broadcast('totalSeatListModification');
      this.activeModal.close();
    });
  }
}
