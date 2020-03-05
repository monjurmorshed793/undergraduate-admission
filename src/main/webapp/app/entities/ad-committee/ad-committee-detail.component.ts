import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdCommittee } from 'app/shared/model/ad-committee.model';

@Component({
  selector: 'jhi-ad-committee-detail',
  templateUrl: './ad-committee-detail.component.html'
})
export class AdCommitteeDetailComponent implements OnInit {
  adCommittee: IAdCommittee | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adCommittee }) => (this.adCommittee = adCommittee));
  }

  previousState(): void {
    window.history.back();
  }
}
