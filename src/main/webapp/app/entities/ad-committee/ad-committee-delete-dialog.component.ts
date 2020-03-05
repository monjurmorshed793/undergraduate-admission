import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAdCommittee } from 'app/shared/model/ad-committee.model';
import { AdCommitteeService } from './ad-committee.service';

@Component({
  templateUrl: './ad-committee-delete-dialog.component.html'
})
export class AdCommitteeDeleteDialogComponent {
  adCommittee?: IAdCommittee;

  constructor(
    protected adCommitteeService: AdCommitteeService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adCommitteeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('adCommitteeListModification');
      this.activeModal.close();
    });
  }
}
