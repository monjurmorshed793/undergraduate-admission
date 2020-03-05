import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdCommittee } from 'app/shared/model/ad-committee.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AdCommitteeService } from './ad-committee.service';
import { AdCommitteeDeleteDialogComponent } from './ad-committee-delete-dialog.component';

@Component({
  selector: 'jhi-ad-committee',
  templateUrl: './ad-committee.component.html'
})
export class AdCommitteeComponent implements OnInit, OnDestroy {
  adCommittees: IAdCommittee[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected adCommitteeService: AdCommitteeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.adCommittees = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.adCommitteeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IAdCommittee[]>) => this.paginateAdCommittees(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.adCommittees = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAdCommittees();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAdCommittee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAdCommittees(): void {
    this.eventSubscriber = this.eventManager.subscribe('adCommitteeListModification', () => this.reset());
  }

  delete(adCommittee: IAdCommittee): void {
    const modalRef = this.modalService.open(AdCommitteeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.adCommittee = adCommittee;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAdCommittees(data: IAdCommittee[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.adCommittees.push(data[i]);
      }
    }
  }
}
