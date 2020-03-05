import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFaProgram } from 'app/shared/model/fa-program.model';

@Component({
  selector: 'jhi-fa-program-detail',
  templateUrl: './fa-program-detail.component.html'
})
export class FaProgramDetailComponent implements OnInit {
  faProgram: IFaProgram | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faProgram }) => (this.faProgram = faProgram));
  }

  previousState(): void {
    window.history.back();
  }
}
