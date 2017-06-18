import { TestBed, inject } from '@angular/core/testing';

import { ClientConsultationService } from './client-consultation.service';

describe('ClientConsultationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ClientConsultationService]
    });
  });

  it('should be created', inject([ClientConsultationService], (service: ClientConsultationService) => {
    expect(service).toBeTruthy();
  }));
});
