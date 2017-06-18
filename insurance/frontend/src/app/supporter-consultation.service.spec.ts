import { TestBed, inject } from '@angular/core/testing';

import { SupporterConsultationService } from './supporter-consultation.service';

describe('SupporterConsultationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SupporterConsultationService]
    });
  });

  it('should be created', inject([SupporterConsultationService], (service: SupporterConsultationService) => {
    expect(service).toBeTruthy();
  }));
});
