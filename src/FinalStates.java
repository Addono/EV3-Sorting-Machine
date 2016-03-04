
enum FinalStates implements FinalState {
	Exit {
		@Override
		public FinalState next(Input i, Output o) {
			return Exit;
		}
	}
}