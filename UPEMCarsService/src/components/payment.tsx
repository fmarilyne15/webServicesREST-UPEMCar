import { withStyles, WithStyles } from '@material-ui/core/styles';

import InputBase from '@material-ui/core/InputBase';
import Modal from '@material-ui/core/Modal';

import * as React from 'react';

const paymentDecorated = withStyles(theme =>({
    inputBase: {
        width: theme.spacing.unit * 50,
    },
    paper: {
        backgroundColor: theme.palette.background.paper,
        boxShadow: theme.shadows[5],
        padding: theme.spacing.unit * 4,
        position: 'absolute',
        width: theme.spacing.unit * 50,
    }
}));

interface IPayementProps{
    readonly open: boolean,
}

export const Payment = paymentDecorated(class extends React.Component<IPayementProps & WithStyles<'inputBase' | 'paper'>,{}>{
    constructor(props: any){
        super(props);
    }

    public render() {
       const { classes, open } = this.props;
    
        return (
          <div className={classes.paper}>
            <Modal
              aria-labelledby="simple-modal-title"
              aria-describedby="simple-modal-description"
              open={open}
            //  onClose={this.handleClose}
            >
                  <InputBase className={classes.inputBase} placeholder="Numéro de carte crédit" /> <br/>
                  <InputBase className={classes.inputBase} defaultValue="Cryptogramme" />
            </Modal>
          </div>
        );
      }
}); 