import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFaProgram } from 'app/shared/model/fa-program.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FaProgramService } from './fa-program.service';
import { FaProgramDeleteDialogComponent } from './fa-program-delete-dialog.component';

@Component({
  selector: 'jhi-fa-program',
  templateUrl: './fa-program.component.html'
})
export class FaProgramComponent implements OnInit, OnDestroy {
  faPrograms: IFaProgram[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected faProgramService: FaProgramService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.faPrograms = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.faProgramService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFaProgram[]>) => this.paginateFaPrograms(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.faPrograms = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFaPrograms();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFaProgram): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFaPrograms(): void {
    this.eventSubscriber = this.eventManager.subscribe('faProgramListModification', () => this.reset());
  }

  delete(faProgram: IFaProgram): void {
    const modalRef = this.modalService.open(FaProgramDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.faProgram = faProgram;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFaPrograms(data: IFaProgram[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.faPrograms.push(data[i]);
      }
    }
  }
}
