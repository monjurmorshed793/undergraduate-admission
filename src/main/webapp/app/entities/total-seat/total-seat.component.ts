import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITotalSeat } from 'app/shared/model/total-seat.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TotalSeatService } from './total-seat.service';
import { TotalSeatDeleteDialogComponent } from './total-seat-delete-dialog.component';

@Component({
  selector: 'jhi-total-seat',
  templateUrl: './total-seat.component.html'
})
export class TotalSeatComponent implements OnInit, OnDestroy {
  totalSeats: ITotalSeat[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected totalSeatService: TotalSeatService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.totalSeats = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.totalSeatService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITotalSeat[]>) => this.paginateTotalSeats(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.totalSeats = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTotalSeats();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITotalSeat): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTotalSeats(): void {
    this.eventSubscriber = this.eventManager.subscribe('totalSeatListModification', () => this.reset());
  }

  delete(totalSeat: ITotalSeat): void {
    const modalRef = this.modalService.open(TotalSeatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.totalSeat = totalSeat;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTotalSeats(data: ITotalSeat[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.totalSeats.push(data[i]);
      }
    }
  }
}
