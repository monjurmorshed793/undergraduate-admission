import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITotalSeat } from 'app/shared/model/total-seat.model';

@Component({
  selector: 'jhi-total-seat-detail',
  templateUrl: './total-seat-detail.component.html'
})
export class TotalSeatDetailComponent implements OnInit {
  totalSeat: ITotalSeat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ totalSeat }) => (this.totalSeat = totalSeat));
  }

  previousState(): void {
    window.history.back();
  }
}
